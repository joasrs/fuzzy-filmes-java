package ScoreMovieFuzzy;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MainScoreMovie {
	public static void main(String[] args) {
		try {	
			List<Filme> filmes = new ArrayList<>();
			
			GrupoVariaveis grupoPontuacaoGenero = new GrupoVariaveis();
			grupoPontuacaoGenero.add(new VariavelFuzzy("GeneroMuitoRuim",0,0,20,30));
			grupoPontuacaoGenero.add(new VariavelFuzzy("GeneroRuim",20,40,60,80));
			grupoPontuacaoGenero.add(new VariavelFuzzy("GeneroBom",40,80,90,100));
			grupoPontuacaoGenero.add(new VariavelFuzzy("GeneroMuitoBom",80,96,100,100));
			
			GrupoVariaveis grupoClassificacao = new GrupoVariaveis();
			grupoClassificacao.add(new VariavelFuzzy("ClaMuitoRuim",0,0,2,3));
			grupoClassificacao.add(new VariavelFuzzy("ClaRuim",2,4,6,8));
			grupoClassificacao.add(new VariavelFuzzy("ClaBoa",4,8,9,10));
			grupoClassificacao.add(new VariavelFuzzy("ClaMuitoBoa",8,9,10,10));
			
			GrupoVariaveis grupoAtratividade = new GrupoVariaveis();
			grupoAtratividade.add(new VariavelFuzzy("NaoAssistir",0,0,3,6));
			grupoAtratividade.add(new VariavelFuzzy("TalvezAssistir",5,7,8,10));
			grupoAtratividade.add(new VariavelFuzzy("Assistir",7,9,10,10));
			
	        BufferedReader bfr = new BufferedReader(new FileReader("C:\\\\Users\\\\joasr\\\\eclipse-workspace\\\\ScoreMovieFuzzy\\\\src\\\\ScoreMovieFuzzy\\\\movie_dataset.txt"));
	        
	        String line = "";
			
	        while((line=bfr.readLine())!=null) {     
	        	String[] valores = line.split(";");
	        	String nomeFilme = valores[0];
	        	String generoFilme = valores[1];
                float classificacaoFilme = Float.parseFloat( valores[2] );
            	float pontuacaoGenero = 0;
                
                if(generoFilme.contains("Thriller")) {
                	pontuacaoGenero = 80;
                }
                
                if(generoFilme.contains("Drama") && pontuacaoGenero == 0) {
                	pontuacaoGenero = 78;
                }
                
                if(generoFilme.contains("Action") && pontuacaoGenero == 0) {
                	pontuacaoGenero = 70;
                }
                
                if(generoFilme.contains("Science Fiction") && pontuacaoGenero == 0) {
                	pontuacaoGenero = 65;
                }
                
                if(pontuacaoGenero == 0) {
                	pontuacaoGenero = 10;
                }
                
                HashMap<String, Float> resultadosFuzzy = new HashMap<>();
                
				grupoPontuacaoGenero.fuzzifica(pontuacaoGenero, resultadosFuzzy);
				grupoClassificacao.fuzzifica(classificacaoFilme, resultadosFuzzy);
				
				rodaRegraE(resultadosFuzzy,"GeneroMuitoRuim","ClaMuitoRuim","NaoAssistir");
				rodaRegraE(resultadosFuzzy,"GeneroMuitoRuim","ClaRuim","NaoAssistir");
				rodaRegraE(resultadosFuzzy,"GeneroMuitoRuim","ClaBoa","TalvezAssistir");
				rodaRegraE(resultadosFuzzy,"GeneroMuitoRuim","ClaMuitoBoa","TalvezAssistir");
				
				rodaRegraE(resultadosFuzzy,"GeneroRuim","ClaMuitoRuim","NaoAssistir");
				rodaRegraE(resultadosFuzzy,"GeneroRuim","ClaRuim","NaoAssistir");
				rodaRegraE(resultadosFuzzy,"GeneroRuim","ClaBoa","TalvezAssistir");
				rodaRegraE(resultadosFuzzy,"GeneroRuim","ClaMuitoBoa","TalvezAssistir");
				
				rodaRegraE(resultadosFuzzy,"GeneroBom","ClaMuitoRuim","NaoAssistir");
				rodaRegraE(resultadosFuzzy,"GeneroBom","ClaRuim","NaoAssistir");
				rodaRegraE(resultadosFuzzy,"GeneroBom","ClaBoa","Assistir");
				rodaRegraE(resultadosFuzzy,"GeneroBom","ClaMuitoBoa","Assistir");
				
				rodaRegraE(resultadosFuzzy,"GeneroMuitoBom","ClaMuitoRuim","NaoAssistir");
				rodaRegraE(resultadosFuzzy,"GeneroMuitoBom","ClaRuim","TalvezAssistir");
				rodaRegraE(resultadosFuzzy,"GeneroMuitoBom","ClaBoa","Assistir");
				rodaRegraE(resultadosFuzzy,"GeneroMuitoBom","ClaMuitoBoa","Assistir");
				
				float NaoAssistir = resultadosFuzzy.get("NaoAssistir");
				float TalvezAssistir = resultadosFuzzy.get("TalvezAssistir");
				float Assistir = resultadosFuzzy.get("Assistir");
				
				float score = (NaoAssistir*1.5f+TalvezAssistir*7.0f+Assistir*9.5f)/(NaoAssistir+TalvezAssistir+Assistir);
								
	            filmes.add(new Filme(nomeFilme, generoFilme, score, classificacaoFilme));
			}
			
	        filmes.sort((filme1, filme2) -> Float.compare(filme2.getScore(), filme1.getScore()));
	        
	        for (int i = 0; i < 1000; i++) {
	        	Filme filme = filmes.get(i);
	        	System.out.println("Rank " + (i + 1) + ": " + filme.getNome() + ", Gênero: " + filme.getGenero() + " - Score: " + filme.getScore() + " - Classificação: " + filme.getClassificacao());
	        }	    
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void rodaRegraE(HashMap<String, Float> asVariaveis,String var1,String var2,String varr) {
		float v = Math.min(asVariaveis.get(var1),asVariaveis.get(var2));
		if(asVariaveis.keySet().contains(varr)) {
			float vatual = asVariaveis.get(varr);
			asVariaveis.put(varr, Math.max(vatual, v));
		}else {
			asVariaveis.put(varr, v);
		}
	}
}
