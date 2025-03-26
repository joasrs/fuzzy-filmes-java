package ScoreMovieFuzzy;

import java.util.ArrayList;
import java.util.HashMap;

public class GrupoVariaveis {
	ArrayList<VariavelFuzzy> listaDeVariaveis;

    public GrupoVariaveis() {
        listaDeVariaveis = new ArrayList<>();
    }

    public void add(VariavelFuzzy var) {
        listaDeVariaveis.add(var);
    }

    public void fuzzifica(float v, HashMap<String, Float> variaveisFuzzy) {
        for (VariavelFuzzy var : listaDeVariaveis) {
            float val = var.fuzzifica(v);
            variaveisFuzzy.put(var.nome, val);
        }
    }
}
