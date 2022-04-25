package jeu_de_tir;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class ScoreBoard {
    
    /*function to add a string to a file*/
    public static void AddtoScoreList(String m,String n) {
        String aAjouter = m;
        FileWriter writer = null;
        try {
            writer = new FileWriter(n, true);
            writer.write(aAjouter, 0, aAjouter.length());
            } catch (IOException ex) {
            ex.printStackTrace();
            } finally {
            if (writer != null) {
                try {
                    writer.close();
                    } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /*function to make the file*/
    public static void create(String n) {boolean bool = false;
        try {if (IsEmpty(n)==false) bool=true;}
        catch (Exception e) {e.printStackTrace();}
        if (bool == false)
        new File(n);
    };
    /*function to read the file*/
    public static List<String> Read(String n) throws IOException {
        String pathFichier=n;
        BufferedReader reader = new BufferedReader(new FileReader(pathFichier));
        List<String> lines = new ArrayList<String>();
        String line;
        while((line = reader.readLine()) != null) {
            lines.add(line);
            
        }
        reader.close();
        return lines;
    }/*function to reset a file (mainly for testing)*/
    public static void reset(String n) {
        File fichier = new File(n);
        fichier.delete();
        new File(n);
        AddtoScoreList("Score Board\n",n);
    }
    /*function to open a file (also testing)*/
    public static void ouvrir(String n) throws IOException{
        File fichier = new File(n);
        Desktop desktop = Desktop.getDesktop();
        desktop.open(fichier);
    }
    /*function to sort the scores and add them to a file*/
    public static void sortScoreboard(String n) {
        String aux = new String();
        int aux1;
        aux = " ";
        int L = 1;
        List<String> lines = new ArrayList<String>();
        String line;
            try{
            BufferedReader br = new BufferedReader(new FileReader(n));
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            br.close();}
            catch (IOException e){e.printStackTrace();}
          
        List<Integer> scoreBoardScores = new ArrayList<Integer>();
        for (int i = 1; i < lines.size(); i++) {
            String[] splitData = lines.get(i).split("\\s{2,}");
          scoreBoardScores.add(Integer.valueOf(splitData[1]));
        }
        if (lines.size()>2){        
        while (L != 0) {
            L = 0;
            for (int i=2 ; i < lines.size(); i++) {
                if (scoreBoardScores.get(i-1) > scoreBoardScores.get(i - 2))
                {  
                    aux1 = scoreBoardScores.get(i-1 );
                    scoreBoardScores.set(i-1 , scoreBoardScores.get(i - 2));
                    scoreBoardScores.set(i - 2, aux1);
                    aux = lines.get(i);
                    lines.set(i, lines.get(i - 	1));
                    lines.set((i - 1), aux);
                    L += 1;
                }
            }
        } 
        reset(n);
 		 for (int j =0; (j<lines.size()-1 && j<10); j++)
        		{
        			 AddtoScoreList((j+1)+"/  "+scoreBoardScores.get(j)+"\n",n);
        		}}
  	 	}
    /*checks if a file is empty*/
    public static boolean IsEmpty(String n) throws Exception
    { Boolean b=false;
        List<String> lines = new ArrayList<String>();
        String line;
        try{
            BufferedReader br = new BufferedReader(new FileReader(n));
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        br.close();}
        catch (IOException e) {
            e.printStackTrace();
        }
        if (lines.size() < 2 )
        b=true;
        return (b);
    }
}