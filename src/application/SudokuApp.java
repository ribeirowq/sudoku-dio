package application;
import java.util.Scanner;

public class SudokuApp {
    private static final int TAMANHO = 9;
    private Celula[][] tabuleiro;
    private Scanner scanner;

    public static void main(String[] args) {
        SudokuApp jogo = new SudokuApp();
        jogo.inicializar(args);
        jogo.executar();
    }

    private void inicializar(String[] args) {
        tabuleiro = new Celula[TAMANHO][TAMANHO];
        scanner = new Scanner(System.in);

        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                tabuleiro[i][j] = new Celula(0, false);
            }
        }

        if (args != null) {
            for (String arg : args) {
                try {
                    String[] partes = arg.split(",");
                    if (partes.length == 3) {
                        int linha = Integer.parseInt(partes[0]) - 1;
                        int coluna = Integer.parseInt(partes[1]) - 1;
                        int valor = Integer.parseInt(partes[2]);

                        if (linha >= 0 && linha < TAMANHO && coluna >= 0 && coluna < TAMANHO
                                && valor >= 1 && valor <= 9) {
                            tabuleiro[linha][coluna] = new Celula(valor, true);
                        }
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Argumento inválido ignorado: " + arg);
                }
            }
        }
    }

    private void executar() {
        boolean rodando = true;

        System.out.println("Bem-vindo ao Sudoku!");

        while (rodando) {
            exibirMenu();
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    exibirTabuleiro();
                    break;
                case "2":
                    colocarNumero();
                    break;
                case "3":
                    removerNumero();
                    break;
                case "4":
                    verificarStatus();
                    break;
                case "5":
                    limparTabuleiro();
                    break;
                case "6":
                    rodando = tentarFinalizar();
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
        scanner.close();
    }

    private void exibirMenu() {
        System.out.println("\n=== MENU ===");
        System.out.println("1. Exibir tabuleiro");
        System.out.println("2. Colocar número");
        System.out.println("3. Remover número");
        System.out.println("4. Verificar status");
        System.out.println("5. Limpar tabuleiro");
        System.out.println("6. Finalizar jogo");
        System.out.println("============");
    }

    private void exibirTabuleiro() {
        System.out.println("\n  | 1 2 3 | 4 5 6 | 7 8 9");
        System.out.println("--+-------+-------+-------");

        for (int i = 0; i < TAMANHO; i++) {
            System.out.print((i+1) + " | ");
            for (int j = 0; j < TAMANHO; j++) {
                Celula celula = tabuleiro[i][j];
                System.out.print(celula.isFixo() ? "[" + celula.getValor() + "]"
                        : celula.estaVazia() ? " . " : " " + celula.getValor() + " ");

                if ((j + 1) % 3 == 0) System.out.print("| ");
            }
            System.out.println();
            if ((i + 1) % 3 == 0) System.out.println("--+-------+-------+-------");
        }
    }

    private void colocarNumero() {
        System.out.println("\nColocar número (formato: linha coluna valor)");
        System.out.print("Digite: ");

        try {
            String[] partes = scanner.nextLine().split(" ");
            if (partes.length != 3) throw new IllegalArgumentException();

            int linha = Integer.parseInt(partes[0]) - 1;
            int coluna = Integer.parseInt(partes[1]) - 1;
            int valor = Integer.parseInt(partes[2]);

            if (linha < 0 || linha >= TAMANHO || coluna < 0 || coluna >= TAMANHO) {
                System.out.println("Posição inválida! Use valores entre 1 e 9.");
                return;
            }

            Celula celula = tabuleiro[linha][coluna];
            if (celula.isFixo()) {
                System.out.println("Não é possível alterar números fixos!");
                return;
            }

            if (!celula.estaVazia()) {
                System.out.println("Posição já ocupada! Remova o número primeiro.");
                return;
            }

            if (valor < 1 || valor > 9) {
                System.out.println("Valor inválido! Use números entre 1 e 9.");
                return;
            }

            if (!podeInserir(linha, coluna, valor)) {
                System.out.println("Este número viola as regras do Sudoku!");
                return;
            }

            celula.setValor(valor);
            System.out.println("Número colocado com sucesso!");
            exibirTabuleiro();

        } catch (Exception e) {
            System.out.println("Entrada inválida! Use o formato: linha coluna valor");
        }
    }

    private boolean podeInserir(int linha, int coluna, int valor) {
        // Verifica linha
        for (int j = 0; j < TAMANHO; j++) {
            if (j != coluna && tabuleiro[linha][j].getValor() == valor) {
                return false;
            }
        }

        // Verifica coluna
        for (int i = 0; i < TAMANHO; i++) {
            if (i != linha && tabuleiro[i][coluna].getValor() == valor) {
                return false;
            }
        }

        int linhaInicio = (linha / 3) * 3;
        int colunaInicio = (coluna / 3) * 3;

        for (int i = linhaInicio; i < linhaInicio + 3; i++) {
            for (int j = colunaInicio; j < colunaInicio + 3; j++) {
                if (i != linha && j != coluna && tabuleiro[i][j].getValor() == valor) {
                    return false;
                }
            }
        }

        return true;
    }

    private void removerNumero() {
        System.out.println("\nRemover número (formato: linha coluna)");
        System.out.print("Digite: ");

        try {
            String[] partes = scanner.nextLine().split(" ");
            if (partes.length != 2) throw new IllegalArgumentException();

            int linha = Integer.parseInt(partes[0]) - 1;
            int coluna = Integer.parseInt(partes[1]) - 1;

            if (linha < 0 || linha >= TAMANHO || coluna < 0 || coluna >= TAMANHO) {
                System.out.println("Posição inválida! Use valores entre 1 e 9.");
                return;
            }

            Celula celula = tabuleiro[linha][coluna];
            if (celula.isFixo()) {
                System.out.println("Não é possível remover números fixos!");
                return;
            }

            if (celula.estaVazia()) {
                System.out.println("Esta posição já está vazia!");
                return;
            }

            celula.setValor(0);
            System.out.println("Número removido com sucesso!");
            exibirTabuleiro();

        } catch (Exception e) {
            System.out.println("Entrada inválida! Use o formato: linha coluna");
        }
    }

    private void verificarStatus() {
        boolean completo = true;
        boolean temErros = false;

        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                Celula celula = tabuleiro[i][j];

                if (celula.estaVazia()) {
                    completo = false;
                } else {
                    int valorTemp = celula.getValor();
                    celula.setValor(0);
                    boolean valido = podeInserir(i, j, valorTemp);
                    celula.setValor(valorTemp);

                    if (!valido) temErros = true;
                }
            }
        }

        System.out.println("\nStatus do Jogo:");
        if (!foiIniciado()) {
            System.out.println("- Jogo não iniciado");
        } else if (completo) {
            System.out.println("- Jogo completo!");
            System.out.println(temErros ? "- Contém erros!" : "- Parabéns! Solução correta!");
        } else {
            System.out.println("- Jogo em andamento");
            System.out.println(temErros ? "- Contém erros!" : "- Sem erros detectados");
        }
    }

    private boolean foiIniciado() {
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if (!tabuleiro[i][j].estaVazia()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void limparTabuleiro() {
        int removidos = 0;
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if (!tabuleiro[i][j].isFixo() && !tabuleiro[i][j].estaVazia()) {
                    tabuleiro[i][j].setValor(0);
                    removidos++;
                }
            }
        }
        System.out.println("\n" + removidos + " números removidos. Tabuleiro limpo!");
        exibirTabuleiro();
    }

    private boolean tentarFinalizar() {
        verificarStatus();

        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if (tabuleiro[i][j].estaVazia()) {
                    System.out.println("Preencha todos os espaços antes de finalizar!");
                    return true;
                }
            }
        }

        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                Celula celula = tabuleiro[i][j];
                int valorTemp = celula.getValor();
                celula.setValor(0);
                boolean valido = podeInserir(i, j, valorTemp);
                celula.setValor(valorTemp);

                if (!valido) {
                    System.out.println("Corrija os conflitos antes de finalizar!");
                    return true;
                }
            }
        }

        System.out.println("\nParabéns! Você completou o Sudoku corretamente!");
        return false;
    }
}