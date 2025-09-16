import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

// Felipe Karmann, Nicholas Nigro
public class AnalisadorGrafo {

	// Método para classificar o tipo do grafo
	public static String tipoDoGrafo(int[][] matriz) {
		boolean dirigido = false;
		boolean multigrafo = false;
		boolean completo = true;
		boolean nulo = true;
		int n = matriz.length;
		

		// Verificar se é dirigido
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (matriz[i][j] != matriz[j][i]) {
					dirigido = true;
					break;
				}
			}
			if (dirigido)
				break;
		}

		// Verificar se é multigrafo, completo, nulo
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (matriz[i][j] > 1 && i != j) {
					multigrafo = true;
				}
				if (matriz[i][j] > 0) {
					nulo = false;
				}
				if (i != j && matriz[i][j] == 0) {
					completo = false;
				}
			}
		}

		// Verificar se é regular
		boolean regular = true;
		if (!nulo) {
			int primeiroGrau = -1;
			for (int i = 0; i < n; i++) {
				int grau = 0;
				for (int j = 0; j < n; j++) {
					if (matriz[i][j] > 0) {
						if (i == j) {
							grau += 2; // laço conta como 2
						} else {
							grau += 1; // aresta normal conta como 1
						}
					}
				}

				if (primeiroGrau == -1) {
					primeiroGrau = grau;
				} else if (grau != primeiroGrau) {
					regular = false;
					break;
				}
			}
		} else {
			regular = true; // grafo nulo é regular por definição
		}

		// Construir a string de classificação
		StringBuilder resultado = new StringBuilder();
		resultado.append(dirigido ? "Dirigido" : "Não-dirigido");
		resultado.append(", ");
		resultado.append(multigrafo ? "Multigrafo" : "Simples");

		if (nulo) {
			resultado.append(", Nulo");
		} else if (completo) {
			resultado.append(", Completo");
		} else if (regular) {
			resultado.append(", Regular");
		}

		return resultado.toString();
	}

	// Método para listar arestas do grafo
	public static String arestasDoGrafo(int[][] matriz) {
		int n = matriz.length;
		int totalArestas = 0;
		Set<String> conjuntoArestas = new LinkedHashSet<>();
		boolean dirigido = false;

		// Verificar se é dirigido
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (matriz[i][j] != matriz[j][i]) {
					dirigido = true;
					break;
				}
			}
			if (dirigido)
				break;
		}

		// Contar arestas e construir conjunto
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (matriz[i][j] > 0) {
					// Para grafos não-dirigidos, considerar apenas metade da matriz
					if (!dirigido && i > j)
						continue;

					String aresta;
					if (dirigido) {
						aresta = "(" + i + "→" + j + ")";
					} else {
						aresta = "(" + i + "-" + j + ")";
					}

					// Adicionar múltiplas vezes para arestas paralelas
					for (int k = 0; k < matriz[i][j]; k++) {
						conjuntoArestas.add(aresta);
						totalArestas++;
					}
				}
			}
		}

		return "Quantidade de arestas: " + totalArestas + "\nConjunto de arestas: " + conjuntoArestas.toString();
	}

	// Método para calcular graus dos vértices - CORRIGIDO
	public static String grausDoVertice(int[][] matriz) {
		int n = matriz.length;
		boolean dirigido = false;

		// Verificar se é dirigido
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (matriz[i][j] != matriz[j][i]) {
					dirigido = true;
					break;
				}
			}
			if (dirigido)
				break;
		}

		StringBuilder resultado = new StringBuilder();
		int grauGrafo = 0;
		List<Integer> sequenciaGraus = new ArrayList<>();

		if (dirigido) {
			int[] grauEntrada = new int[n];
			int[] grauSaida = new int[n];

			resultado.append("Grafo Dirigido\n");
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					// Grau de saída: soma dos valores da linha
					grauSaida[i] += matriz[i][j];
					// Grau de entrada: soma dos valores da coluna
					grauEntrada[j] += matriz[i][j];
				}

				resultado.append("Vértice ").append(i).append(": grau de entrada = ").append(grauEntrada[i])
						.append(", grau de saída = ").append(grauSaida[i]).append("\n");

				sequenciaGraus.add(grauSaida[i]);
				grauGrafo += grauSaida[i];
			}
		} else {
			int[] graus = new int[n];

			resultado.append("Grafo Não-Dirigido\n");
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (matriz[i][j] > 0) {
						if (i == j) {
							graus[i] += 2 * matriz[i][j]; // Laço conta como 2 para cada ocorrência
						} else {
							graus[i] += matriz[i][j]; // Aresta normal conta como 1 para cada ocorrência
						}
					}
				}

				resultado.append("Vértice ").append(i).append(": grau = ").append(graus[i]).append("\n");

				sequenciaGraus.add(graus[i]);
				grauGrafo += graus[i];
			}
		}

		// Ordenar sequência de graus em ordem decrescente
		Collections.sort(sequenciaGraus, Collections.reverseOrder());

		resultado.append("Grau do grafo: ").append(grauGrafo).append("\n");
		resultado.append("Sequência de graus: ").append(sequenciaGraus.toString());

		return resultado.toString();
	}

	// Método para busca em profundidade
	public static String buscaEmProfundidade(int[][] matriz) {
		int n = matriz.length;
		boolean[] visitados = new boolean[n];
		List<Integer> ordemExploracao = new ArrayList<>();

		for (int i = 0; i < n; i++) {
			if (!visitados[i]) {
				dfs(matriz, i, visitados, ordemExploracao);
			}
		}

		return "Ordem de exploração: " + ordemExploracao.toString();
	}

	// Método auxiliar recursivo para DFS
	private static void dfs(int[][] matriz, int vertice, boolean[] visitados, List<Integer> ordemExploracao) {
		visitados[vertice] = true;
		ordemExploracao.add(vertice);

		for (int i = 0; i < matriz.length; i++) {
			if (matriz[vertice][i] > 0 && !visitados[i]) {
				dfs(matriz, i, visitados, ordemExploracao);
			}
		}
	}

	// Método main para teste
	public static void main(String[] args) {
		// Exemplo de matriz de adjacência (grafo não-dirigido)
		int[][] matrizExemplo = { { 0, 1, 1, 0 }, { 1, 0, 1, 1 }, { 1, 1, 0, 0 }, { 0, 1, 0, 0 } };

		// Exemplo de matriz de adjacência (grafo dirigido)
		int[][] matrizDirigido = { { 0, 1, 0, 0 }, { 0, 0, 1, 1 }, { 1, 0, 0, 0 }, { 0, 0, 0, 0 } };

		// Novo grafo: 4 vértices em formato de quadrado
		// Vértices 0 e 1 têm arestas paralelas entre si
		// Vértice 0 tem um laço
		// Graus: V0=5, V1=3, V2=2, V3=2
		int[][] matrizQuadrado = {
			{ 1, 2, 1, 0 }, // Vértice 0: laço (1) + arestas paralelas para 1 (2) + aresta para 2 (1) = 1*2 + 2 + 1 = 5
			{ 2, 0, 0, 1 }, // Vértice 1: arestas paralelas para 0 (2) + aresta para 3 (1) = 2 + 1 = 3
			{ 1, 0, 0, 1 }, // Vértice 2: aresta para 0 (1) + aresta para 3 (1) = 1 + 1 = 2
			{ 0, 1, 1, 0 }  // Vértice 3: aresta para 1 (1) + aresta para 2 (1) = 1 + 1 = 2
		};

		System.out.println("=== ANÁLISE DO GRAFO NÃO-DIRIGIDO ===\n");

		System.out.println("1. Tipo do Grafo:");
		System.out.println(tipoDoGrafo(matrizExemplo));
		System.out.println();

		System.out.println("2. Arestas do Grafo:");
		System.out.println(arestasDoGrafo(matrizExemplo));
		System.out.println();

		System.out.println("3. Graus dos Vértices:");
		System.out.println(grausDoVertice(matrizExemplo));
		System.out.println();

		System.out.println("4. Busca em Profundidade:");
		System.out.println(buscaEmProfundidade(matrizExemplo));
		System.out.println();

		System.out.println("=== ANÁLISE DO GRAFO DIRIGIDO ===\n");

		System.out.println("1. Tipo do Grafo:");
		System.out.println(tipoDoGrafo(matrizDirigido));
		System.out.println();

		System.out.println("2. Arestas do Grafo:");
		System.out.println(arestasDoGrafo(matrizDirigido));
		System.out.println();

		System.out.println("3. Graus dos Vértices:");
		System.out.println(grausDoVertice(matrizDirigido));
		System.out.println();

		System.out.println("4. Busca em Profundidade:");
		System.out.println(buscaEmProfundidade(matrizDirigido));
		System.out.println();

		System.out.println("=== ANÁLISE DO GRAFO QUADRADO COM ARESTAS PARALELAS E LAÇO ===\n");

		System.out.println("1. Tipo do Grafo:");
		System.out.println(tipoDoGrafo(matrizQuadrado));
		System.out.println();

		System.out.println("2. Arestas do Grafo:");
		System.out.println(arestasDoGrafo(matrizQuadrado));
		System.out.println();

		System.out.println("3. Graus dos Vértices:");
		System.out.println(grausDoVertice(matrizQuadrado));
		System.out.println();

		System.out.println("4. Busca em Profundidade:");
		System.out.println(buscaEmProfundidade(matrizQuadrado));
	}
}
