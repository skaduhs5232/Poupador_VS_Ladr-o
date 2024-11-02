package agente;

import algoritmo.ProgramaLadrao;
import java.awt.Point;

public class Ladrao extends ProgramaLadrao {

	private Point posicaoPoupador = null; // Última posição conhecida do poupador

	public int acao() {
		int[] visao = sensor.getVisaoIdentificacao(); // Visão ao redor do ladrão
		int[] olfatoPoupador = sensor.getAmbienteOlfatoPoupador(); // Olfato do poupador
		Point posicaoAtual = sensor.getPosicao(); // Posição do ladrão

		// Tentar seguir o poupador se ele for visível
		if (seguirPoupador(visao)) {
			return moverParaPosicao(posicaoAtual, posicaoPoupador); // Segue o poupador
		}

		// Usar olfato para seguir a direção do poupador
		if (seguirPoupadorPeloOlfato(olfatoPoupador)) {
			return moverParaPosicao(posicaoAtual, posicaoPoupador);
		}

		// Caso contrário, patrulha ou movimento aleatório
		return patrulhar(posicaoAtual);
	}

	// Método para seguir o poupador se ele for visível
	private boolean seguirPoupador(int[] visao) {
		for (int i = 0; i < visao.length; i++) {
			if (visao[i] == 100) { // 100 = poupador
				posicaoPoupador = calcularPosicaoPoupador(i);
				return true; // Encontrou o poupador
			}
		}
		return false; // Poupador não encontrado
	}

	// Método para seguir o poupador pelo olfato
	private boolean seguirPoupadorPeloOlfato(int[] olfatoPoupador) {
		int maiorCheiro = 0;
		int indiceMaiorCheiro = -1;
		for (int i = 0; i < olfatoPoupador.length; i++) {
			if (olfatoPoupador[i] > maiorCheiro) {
				maiorCheiro = olfatoPoupador[i];
				indiceMaiorCheiro = i;
			}
		}

		if (indiceMaiorCheiro != -1) { // Se há cheiro, mover na direção
			posicaoPoupador = calcularPosicaoOlfato(indiceMaiorCheiro);
			return true; // Seguir pelo olfato
		}
		return false; // Não há cheiro
	}

	// Método para calcular a posição do poupador baseado na visão
	private Point calcularPosicaoPoupador(int indiceVisao) {
		int x = sensor.getPosicao().x;
		int y = sensor.getPosicao().y;

		switch (indiceVisao) {
			case 0: case 1: case 2:
				return new Point(x, y - 1); // Poupador está acima
			case 10: case 11: case 12:
				return new Point(x, y + 1); // Poupador está abaixo
			case 5: case 6: case 7:
				return new Point(x + 1, y); // Poupador está à direita
			case 15: case 16: case 17:
				return new Point(x - 1, y); // Poupador está à esquerda
			default:
				return null;
		}
	}

	// Método para calcular a posição do poupador baseado no olfato
	private Point calcularPosicaoOlfato(int indiceOlfato) {
		int x = sensor.getPosicao().x;
		int y = sensor.getPosicao().y;

		switch (indiceOlfato) {
			case 0: case 1: case 2:
				return new Point(x, y - 1); // Cheiro de cima
			case 10: case 11: case 12:
				return new Point(x, y + 1); // Cheiro de baixo
			case 5: case 6: case 7:
				return new Point(x + 1, y); // Cheiro da direita
			case 15: case 16: case 17:
				return new Point(x - 1, y); // Cheiro da esquerda
			default:
				return null;
		}
	}

	// Método para mover o ladrão até o poupador
	private int moverParaPosicao(Point posicaoAtual, Point posicaoDestino) {
		if (posicaoDestino == null) {
			return 0; // Parado se não houver destino válido
		}

		// Captura se estiver na mesma posição
		if (posicaoAtual.equals(posicaoDestino)) {
			return 0; // Capturou o poupador
		}

		// Movimenta-se na direção do poupador
		if (posicaoAtual.x < posicaoDestino.x) {
			return 3; // Mover para a direita
		} else if (posicaoAtual.x > posicaoDestino.x) {
			return 4; // Mover para a esquerda
		} else if (posicaoAtual.y < posicaoDestino.y) {
			return 2; // Mover para baixo
		} else if (posicaoAtual.y > posicaoDestino.y) {
			return 1; // Mover para cima
		}
		return 0; // Parado se já estiver no destino
	}

	// Patrulha se não tiver pistas
	private int patrulhar(Point posicaoAtual) {
		// Adicione lógica para patrulhar áreas específicas
		return (int) (Math.random() * 5); // Movimento aleatório
	}
}