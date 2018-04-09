package gr.uoa.di.rdf.geographica.strabon;
/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Copyright (C) 2013, Pyravlos Team
 *
 */

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.TupleQueryResultHandlerException;

import eu.earthobservatory.runtime.postgis.Strabon;
import gr.uoa.di.rdf.Geographica.systemsundertest.SystemUnderTest;

/**
 * @author George Garbis <ggarbis@di.uoa.gr>
 * 
 * Implements the interface between the benchmark Geographica and the RDF store Strabon
 */
public class StrabonSUT implements SystemUnderTest {

	static Logger logger = Logger.getLogger(StrabonSUT.class.getSimpleName());
	
	private Strabon strabon = null; // An instance of Strabon
	private BindingSet firstBindingSet; // It holds the first binding of every evaluated query

	String db = null; // Spatially enabled PostGIS database where data is stored 
	String user = null; // Username to connect to database
	String passwd = null; // Password to connect to database
	Integer port = null; // Database host to connect to
	String host = null; // Port to connect to on the database host
	
	// A simple constructor
	public StrabonSUT(String db, String user, String passwd, Integer port,
			String host) throws Exception {
		
		this.db = db;
		this.user = user;
		this.passwd = passwd;
		this.port = port;
		this.host = host;
	}

	// Return the first binding of the evaluated query
	public BindingSet getFirstBindingSet() {return firstBindingSet;}
	
	// Return the instance of Strabon that is used to evaluate queries
	public Object getSystem() {
		return this.strabon;
	}   
	
	// Initialize Strabon
	public void initialize()  {

		try {
			// Create a new instance of Strabon
			strabon = new Strabon(db, user, passwd, port, host, true);
		} catch (Exception e) {
			logger.fatal("Cannot initialize strabon");
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.fatal(stacktrace);
		}
	}

	// Executer to evaluate the given query with time out limit 
	static class Executor implements Runnable {
		private String query; // The query to be evaluated
		private Strabon strabon; // The Strabon instance which will be used to evaluate the query
		private long[] returnValue; // Stores run times 
		private BindingSet firstBindingSet; // Stores the first binding of the evaluation
		
		public Executor(String query, Strabon strabon, int timeoutSecs) {
			this.query = query;
			this.strabon = strabon;
			this.returnValue = new long[]{timeoutSecs+1, timeoutSecs+1, timeoutSecs+1, -1};
		}
		
		public long[] getRetValue() {return returnValue;}
		public BindingSet getFirstBindingSet() {return firstBindingSet;}
		
	    public void run() {	try {
			runQuery();
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		} catch (TupleQueryResultHandlerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} }

	    // Evaluates the given query. Returns four long integers;
	    // 1. Nanoseconds needed to evaluate the query
	    // 2. Nanoseconds needed for a complete iteration over the results
	    // 3. Sum of the two previous times
	    // 4. Count of results
		public void runQuery() throws MalformedQueryException, QueryEvaluationException, TupleQueryResultHandlerException, IOException {

			logger.info("Evaluating query...");
			TupleQuery tupleQuery = (TupleQuery) strabon.query(query,
					eu.earthobservatory.utils.Format.TUQU, strabon
							.getSailRepoConnection(), (OutputStream) System.out);
			
			long results = 0;
			
			long t1 = System.nanoTime();
			TupleQueryResult result = tupleQuery.evaluate();
			long t2 = System.nanoTime();
			
			if (result.hasNext()) { 
				this.firstBindingSet = result.next();
				results++;
			}
			while (result.hasNext()) {
				results++;
				result.next();
			}
			long t3 = System.nanoTime();
			
			logger.info("Query evaluated");
			this.returnValue = new long[]{t2-t1, t3-t2, t3-t1, results};
		}   
	}
	

	// Runs the evaluation of the given query. If the time out limit expires abort 
	// the query evaluation
	public long[] runQueryWithTimeout(String query, int timeoutSecs) {
        final ExecutorService executor = Executors.newFixedThreadPool(1);
        Executor runnable = new Executor(query, strabon, timeoutSecs);
        
        final Future<?> future = executor.submit(runnable);
        boolean isTimedout = false;
		long tt1 = System.nanoTime();
        try {
        	logger.debug("Future started");
			future.get(timeoutSecs, TimeUnit.SECONDS);
			logger.debug("Future end");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			isTimedout = true;
			logger.info("time out!");
			logger.info("Restarting Strabon...");
        	this.restart();
			logger.info("Closing Strabon...");
			this.close();
		}
        finally {
        	logger.debug("Future canceling...");
        	future.cancel(true);
        	logger.debug("Executor shutting down...");
            executor.shutdown();
            try {
            	logger.debug("Executor waiting for termination...");
				executor.awaitTermination(timeoutSecs, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            System.gc();
        }
        
        logger.debug("RetValue: "+runnable.getRetValue());
		
        if (isTimedout) {
			long tt2 = System.nanoTime();
			return new long[]{tt2-tt1, tt2-tt1, tt2-tt1, -1};
		} else {
			this.firstBindingSet = runnable.getFirstBindingSet();
			return runnable.getRetValue();
		}
    }
	
	// Execute updates 
	public long[] runUpdate(String query) throws MalformedQueryException {

		logger.info("Executing update...");
		long t1 = System.nanoTime();
		strabon.update(query, strabon.getSailRepoConnection());
		long t2 = System.nanoTime();
		logger.info("Update executed");
		
		long[] ret = {-1, -1, t2 - t1, -1};
		return ret;
	}

	// Close the Strabon instance
	public void close() {

		logger.info("Closing..");
		try {
			strabon.close();
			strabon = null;
			firstBindingSet = null;
		} catch (Exception e) {}
//
		System.gc();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			logger.fatal("Cannot clear caches");
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.fatal(stacktrace);
		}
		logger.info("Closed (caches not cleared)");
	}

	// Restart Strabon instance (in case of time out)
	public void restart() {
		
		//String[] restart_postgres = {"/bin/sh", "-c" , "service postgresql restart"};
		String[] restart_postgres = {"/bin/sh", "-c" , "echo @ctArine92 | sudo -S service postgresql restart"};
		
		
		
    	Process pr;
    	
		try {
			logger.info("Restarting Strabon (Postgres) ...");

	    	pr = Runtime.getRuntime().exec(restart_postgres);
			pr.waitFor();
			if ( pr.exitValue() != 0) {
				logger.error("Something went wrong while stoping postgres");
			}

			Thread.sleep(5000);
			
			if (strabon != null) {
				try{
					strabon.close();
				} catch (Exception e) {
					logger.error("Exception occured while restarting Strabon. ");
					e.printStackTrace();
				} finally {
					strabon = null;
				}
			}	
			firstBindingSet = null;
			strabon = new Strabon(db,user, passwd, port, host, true);
			logger.info("Strabon (Postgres) restarted");
		} catch (Exception e) {
			logger.fatal("Cannot restart Strabon");
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.fatal(stacktrace);
		}
	}
	
	// Clear caches (to be able to measure times for cold and warm caches)
	public void clearCaches() {
		
		String[] stop_postgres = {"/bin/sh", "-c" , "echo @ctArine92 | sudo -S service postgresql stop"};
    	String[] clear_caches = {"/bin/sh", "-c" , "echo @ctArine92 | sudo -S sync && echo 3 > /proc/sys/vm/drop_caches"};
    	String[] start_postgres = {"/bin/sh", "-c" , "echo @ctArine92 | sudo -S service postgresql start"};

    	Process pr;
    	
		try {
			logger.info("Clearing caches...");

	    	pr = Runtime.getRuntime().exec(stop_postgres);
			pr.waitFor();
			if ( pr.exitValue() != 0) {
				logger.error("Something went wrong while stoping postgres");
			}
			
	    	pr = Runtime.getRuntime().exec(clear_caches);
			pr.waitFor();
			if ( pr.exitValue() != 0) {
				logger.error("Something went wrong while clearing caches");
			}
			
			pr = Runtime.getRuntime().exec(start_postgres);
			pr.waitFor();
			if ( pr.exitValue() != 0) {
				logger.error("Something went wrong while clearing caches");
			}
			
			Thread.sleep(5000);
			logger.info("Caches cleared");
		} catch (Exception e) {
			logger.fatal("Cannot clear caches");
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.fatal(stacktrace);
		}
	}

	@Override
	public String translateQuery(String query, String label) {
		String translatedQuery = null;
		translatedQuery = query;
		
		translatedQuery = translatedQuery.replaceAll("geof:distance", "strdf:distance");
		
		if (label.matches("Get_CLC_areas")
				|| label.matches("Get_highways")
				|| label.matches("Get_municipalities")
				|| label.matches("Get_hotspots")
				|| label.matches("Get_coniferous_forests_in_fire")
				|| label.matches("Get_road_segments_affected_by_fire")) {
			translatedQuery = translatedQuery.replaceAll("<http://www.opengis.net/ont/geosparql#wktLiteral>", "strdf:WKT");			
		}
		
		if (label.matches("List_GeoNames_categories_per_CLC_category")
			|| label.matches("Count_GeoNames_categories_in_ContinuousUrbanFabric")) {
			translatedQuery = translatedQuery.replaceAll(
					" } \\n	FILTER\\(geof:sfIntersects\\(\\?clcWkt, \\?fWkt\\)\\)\\. \\\n",
					" \n	FILTER(geof:sfIntersects(?clcWkt, ?fWkt)). } \n");
		}
		
		return translatedQuery;
	}
	
}
