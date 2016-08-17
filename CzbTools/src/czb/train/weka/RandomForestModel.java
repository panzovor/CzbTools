package czb.train.weka;

import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;

/**
 * Created by E440 on 16-8-16.
 */
public class RandomForestModel extends Model {

    public RandomForestModel() {
        super(new RandomForest());
    }

    public static void main(String[] args){
        RandomForestModel model = new RandomForestModel();
        String options = "-I 100　-K 0 -S 1";
        String data =Parmeter.trainData+"iris.arff";
        String save = Parmeter.modelDir+"randomforest.arff";
        model.evaluateModel(data,10,options);
        model.buildModel_rf(data, options);
        model.saveModel(save);
        String result = model.predict(data);
        model.testModel_rf(data);
//        System.out.println(result);
    }

}
