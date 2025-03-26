package ScoreMovieFuzzy;

public class Filme {
    private String nome;
    private String genero;
    private float score;
    private float classificacao;

    public Filme(String nome, String genero, float score, float classificacao) {
        this.nome = nome;
        this.genero = genero;
        this.score = score;
        this.classificacao = classificacao;
    }

    public String getNome() {
        return nome;
    }

    public String getGenero() {
        return genero;
    }

    public float getScore() {
        return score;
    }
    public float getClassificacao() {
        return Math.round(classificacao * 100.0f) / 100.0f;
    }
}
