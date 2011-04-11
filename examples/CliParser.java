 // http://commons.apache.org/cli/usage.html
// http://commons.apache.org/cli/api-release/index.html
import org.apache.commons.cli.*;

class CliParser{
	public static void main(String[] args) {
				
		// create the command line parser
		CommandLineParser parser = new PosixParser();

		// create the Options
		Options options = new Options();
		options.addOption( "a", "all", false, "do not hide entries starting with ." );
		options.addOption( OptionBuilder.withLongOpt( "block-size" )
		                                .withDescription( "use SIZE-byte blocks" )
		                                .hasArg()
		                                .withArgName("SIZE")
		                                .create() );
		options.addOption( "B", "ignore-backups", false, "do not list implied entried");
		
		try {
		    // parse the command line arguments
		    CommandLine line = parser.parse( options, args );
				
		    // validate that block-size has been set
		    if( line.hasOption( "block-size" ) ) {
		        // print the value of block-size
		        System.out.println( line.getOptionValue( "block-size" ) );
		    }
		
		    if( line.hasOption( "h" ) ) {
				new HelpFormatter().printHelp( "ant", options );
		    }
		}
		catch( ParseException exp ) {
		    System.out.println( "Unexpected exception:" + exp.getMessage() );
		}	
	}
}

