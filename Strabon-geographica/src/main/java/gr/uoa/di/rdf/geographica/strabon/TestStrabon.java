package gr.uoa.di.rdf.geographica.strabon;
import gr.uoa.di.rdf.Geographica.experiments.Experiment;
import gr.uoa.di.rdf.Geographica.experiments.MicroNonTopologicalExperiment;
import gr.uoa.di.rdf.Geographica.experiments.MicroSelectionsExperiment;
import gr.uoa.di.rdf.Geographica.experiments.MicroJoinsExperiment;
import gr.uoa.di.rdf.Geographica.experiments.SyntheticExperiment;
import gr.uoa.di.rdf.Geographica.systemsundertest.SystemUnderTest;

import org.apache.log4j.Logger;

/** 
 * @author George Garbis <ggarbis@di.uoa.gr>
 *
 * This is an example project that uses the benchmark Geographica to test the efficiency of
 * non-topological functions in the RDF store Strabon.
 */
public class TestStrabon {
	
	static Logger logger = Logger.getLogger(TestStrabon.class.getSimpleName());
	
	public static void main(String []args) {
		// The following variables are used to instantiate an experiment

		//-h localhost -db geodatadb -u geouser -P geouser -p 5432 -l /home/chdor/Projects/StoreConnect/benchs/times/strabon -m 90 -t 1800 run MicroNonTopological
		
		int repetitions = 3; // How many times a query will be executed
		int timoutSecs = 1800; //Time out limit. If it expires the query evaluation is aborted.
		String logPath = "/home/chdor/Projects/StoreConnect/benchs/times/strabon"; // Folder where run times are stored
		
		// The following arguments are used to instantiate a StrabonSUT
		String db = "geodatadb"; // Spatially enabled PostGIS database where data is stored
		String user = "geouser"; // Username to connect to database 
		String passwd = "geouser"; // Password to connect to database
		String host = "localhost"; // Database host to connect to
		int port = 5432;           // Port to connect to on the database host
		
		try {
			// Create an instance that represents a Strabon instance
			SystemUnderTest sut = new StrabonSUT(db, user, passwd, port, host);
			// Create an experiment of the micro benchmark that tests non-topological functions
			Experiment experiment = new MicroNonTopologicalExperiment(sut, repetitions, timoutSecs, logPath);
			// Run the experiment
			experiment.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
