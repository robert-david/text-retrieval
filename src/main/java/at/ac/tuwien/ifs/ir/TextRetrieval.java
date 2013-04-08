package at.ac.tuwien.ifs.ir;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exercise 1 - Text Retrieval
 *
 */
public class TextRetrieval {
	
	private static Logger log = LoggerFactory.getLogger(TextRetrieval.class);

    private static String[] topicSet = {
            "misc.forsale/76057",
            "talk.religion.misc/83561",
            "talk.politics.mideast/75422",
            "sci.electronics/53720",
            "sci.crypt/15725",
            "misc.forsale/76165",
            "talk.politics.mideast/76261",
            "alt.atheism/53358",
            "sci.electronics/54340",
            "rec.motorcycles/104389",
            "talk.politics.guns/54328",
            "misc.forsale/76468",
            "sci.crypt/15469",
            "rec.sport.hockey/54171",
            "talk.religion.misc/84177",
            "rec.motorcycles/104727",
            "comp.sys.mac.hardware/52165",
            "sci.crypt/15379",
            "sci.space/60779",
            "sci.med/59456"
    };
	
    public static void main( String[] args ) {

        try {
            if ((args.length < 1) || args[0].equals("--help") || args[0].equals("-h")) {
                TextRetrieval.usage();
                return;
            } else if (args[0].equals("index")) {
                    String source = ".";
                    String target = ".";
                    int blockSize = 100;
                    boolean stemming = false;
                    int upper = -1;
                    int lower = -1;
    
                    try {                    
                        for (int i = 1; i < args.length; i=i+2) {
                            if (args[i].equals("-s") || args[i].equals("--source"))
                                source = args[i+1];
                            else if (args[i].equals("-t") || args[i].equals("--target"))
                                target = args[i+1];
                            else if (args[i].equals("-b") || args[i].equals("--blockSize"))
                                blockSize = new Integer(args[i+1]);
                            else if (args[i].equals("-e") || args[i].equals("--stemming"))
                                stemming = new Boolean(args[i+1]);
                            else if (args[i].equals("-u") || args[i].equals("--upper"))
                                upper = new Integer(args[i+1]);
                            else if (args[i].equals("-l") || args[i].equals("--lower"))
                                lower = new Integer(args[i+1]);
                            else throw new Exception("Illegal option");
                        }
                    } catch (Exception e) {
                        TextRetrieval.usage();
                        return;                    
                    }
                    
                    new Indexer(source, target, blockSize, stemming, upper, lower).index();
                    
                } else if (args[0].equals("weight")) {
                    String indexFile = null;
                    String weightsFile = null;
                    
                    try {                    
                        for (int i = 1; i < args.length; i=i+2) {
                            if (args[i].equals("-i") || args[i].equals("--indexFile"))
                                indexFile = args[i+1];
                            else if (args[i].equals("-w") || args[i].equals("--weightsFile"))
                                weightsFile = args[i+1];
                            else throw new Exception("Illegal option");
                        }
                    } catch (Exception e) {
                        TextRetrieval.usage();
                        return;                    
                    }
                    
                    new TFIDF(indexFile, weightsFile).createWeights();
                    
                } else if (args[0].equals("compare")) {
                    String weightsFile = null;
                    String target = ".";
                    
                    try {                    
                        for (int i = 1; i < args.length; i=i+2) {
                            if (args[i].equals("-w") || args[i].equals("--weightsFile"))
                                weightsFile = args[i+1];
                            else if (args[i].equals("-t") || args[i].equals("--target"))
                                target = args[i+1];
                            else throw new Exception("Illegal option");
                        }
                    } catch (Exception e) {
                        TextRetrieval.usage();
                        return;                    
                    }
                    
                    new CosineSimilarity(weightsFile, target).findSimilar(topicSet);
                    
                } else {
                    TextRetrieval.usage();
                    return;
                }
		} catch (Exception e) {
		    log.error("Error creating index", e);
		}
    }
    
    private static void usage() {
        log.info("Usage: java -jar TextRetrieval.jar command [options] ");
        log.info("where command is one of: index weight compare");
        log.info("index command options include:");
        log.info("-s --source   <source directory for the files to index>");
        log.info("-t --target   <target directory for the index file and the temporary folder>");
        log.info("-b --blockSize <size of the blocks for indexing>");
        log.info("-e --stemming <stemming of words>");
        log.info("-u --upper    <upper bound for frequency thresholding>");
        log.info("-l --lower    <lower bound for frequency thresholding>");
        log.info("weight command options include:");
        log.info("-i --indexFile     <ARFF source file containing the index>");
        log.info("-w --weightsFile   <ARFF target file containing the computed weights>");
        log.info("compare command options include:");
        log.info("-w --weightsFile   <ARFF file containing the weights used in the comparison>");
        log.info("-t --target        <target directory for the similarity result files>");
        log.info("all options include:");
        log.info("-h --help     <print this usage message>");
    }
}
