package application;

public class Celula {
    private int valor;
    private boolean fixo;

    public Celula(int valor, boolean fixo) {
        this.valor = valor;
        this.fixo = fixo;
    }

    public int getValor() {
        return valor;
    }

    public boolean isFixo() {
        return fixo;
    }

    public void setValor(int valor) {
        if (!fixo) {
            this.valor = valor;
        }
    }

    public boolean estaVazia() {
        return valor == 0;
    }

    @Override
    public String toString() {
        return valor == 0 ? " " : String.valueOf(valor);
    }
}

