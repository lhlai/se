package se.preprocess.util;
/**
 * ͣ�ôʴ�����
 * @author pillar
 * @createDate 2016 3.28
 * @version 1.0
 *
 */
public class StopWordHandler {
	private static String stopWordsList[] ={"��", "����","Ҫ","�Լ�","֮","��","��","��","��","��","��","��","Ӧ","��","ĳ","��","��","��","λ","��","һ","��","��","��","��","��","��","��",""};//����ͣ�ô�
    public static boolean IsStopWord(String word)
    {
        for(int i=0;i<stopWordsList.length;++i)
        {
            if(word.equalsIgnoreCase(stopWordsList[i]))
                return true;
        }
        return false;
    }

}
