package czb.train.weka;

import czb.tools.filereader.TxtFileReader;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import java.io.*;
import java.util.Random;

/**
 * Created by E440 on 16-8-15.
 */
public class Model {

    private  TxtFileReader txtFileReader = new TxtFileReader();

    public Model(Classifier  classifier){
        this.classifier = classifier;
    }

    private Classifier classifier = null;

    public String evaluate(Evaluation evaluation){

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(evaluation.toSummaryString()+"\n");
        try {
            stringBuffer.append(evaluation.toMatrixString()+"\n");
            stringBuffer.append(evaluation.toClassDetailsString()+"\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    public String evaluateModel(String trainArff,int numFlot,String options){
        Instances instaces  = null;
        try {
            instaces = new Instances(new FileReader(new File(trainArff)));
            instaces.setClassIndex(instaces.numAttributes()-1);
            Evaluation evaluation = new Evaluation(instaces);
            setOptions(options);
            evaluation.crossValidateModel(classifier,instaces,numFlot,new Random(1));
            return evaluate(evaluation);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setOptions(String options){
        try {
            if(options != null)
                classifier.setOptions(weka.core.Utils.splitOptions(options));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Classifier buildModel_rf(String trainArf,String options) {
        try {
            setOptions(options);
            Instances instaces  = new Instances(new FileReader(new File(trainArf)));
            instaces.setClassIndex(instaces.numAttributes()-1);
            classifier.buildClassifier(instaces);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return classifier;
    }

    public void saveModel(String savepath){
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(savepath)));
            objectOutputStream.writeObject(classifier);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Classifier loadModel_rf(String ModelPath){
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(
                    new FileInputStream(new File(ModelPath)));
            classifier = (RandomForest) ois.readObject();
            return classifier;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String testModel_rf(String testArff){
        try {
            Instances instances = new Instances(new FileReader(new File(testArff)));
            instances.setClassIndex(instances.numAttributes()-1);
            Evaluation evaluation = new Evaluation(instances);
            evaluation.evaluateModel(classifier,instances);
            return evaluate(evaluation);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String predict(String arffFile){
        try {
            Instances instances = new Instances(new FileReader( new File(arffFile)));
            instances.setClassIndex(instances.numAttributes()-1);
            for(int i=0; i< instances.numInstances();i++){
                double value = classifier.classifyInstance(instances.instance(i));
                instances.instance(i).setClassValue(value);
            }
            return instances.toString();
//            System.out.println(instances.toString());
//            return instances;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveResult(String filepath,String content){
        try {
            txtFileReader.write(filepath,content,"utf-8",false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String [] args){

    }


}
