package czb.train.weka;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;

/**
 * Created by E440 on 16-8-15.
 */
public class Csv2Arff {

    public void csv2arff(String csv,String arff) throws IOException {
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(csv));
        Instances data = loader.getDataSet();

        // save ARFF
        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File(arff));
        saver.setDestination(new File(arff));
        saver.writeBatch();
    }

    public static void main(String[] args){}
    



}
