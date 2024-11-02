package agente;

import algoritmo.ProgramaPoupador;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Poupador extends ProgramaPoupador {
	
	public enum MapaPoupador {
		CIMA(1),
		BAIXO (2),
		RIGHT(3),
	  ESQUERDA(4);

	    public final int value ;
	    MapaPoupador(int value) {
	        this.value = value;
	    }

	    public int getValue() {
	    	return value;
	    }
	}

	public enum VisaoPoupador {
		CIMA(7),
		BAIXO (16),
		RIGHT(12),
		ESQUERDA(11);

	    public final int value ;
		VisaoPoupador(int value) {
	        this.value = value;
	    }

	    public int getValue() {
	    	return value;
	    }
	}

    private static final int PRA_FORA = -1;
    private static final int PAREDE = 1;
    private static final int BANCO = 3;
    private static final int MOEDA = 4;
    private static final int PASTILAH_PODER = 5;
    private static final int FREE = 0;
    private static final int BIXO_FEI = 200;
    

    private int irProBanco = 0;
    
    private int tempoSemMoeda;
    private int contagemMoeda = 0; 
    
 

    private double VontadeIrBanco;
    private double irPraLonge;

    private static Point LOCAL_BANCO = new Point(-4, -4);
    
    private String meta;
    private boolean localizacaoDoBanco = false;
    private int acaoAnterior = 0;
    private String nome;

    private HashMap<String, int[]> OlhandoPara = new HashMap<String, int[]>();
    private int[] peso;

    private HashMap<Point, Integer> mapaDoLabirinto = new HashMap<Point, Integer>();

    private HashMap<Integer, int[]> MapeadorFunção = new HashMap<Integer, int[]>();
    private int[] olharCima = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private int[] olharBaixo = {14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
    private int[] olharEsquerda = {0, 1, 5, 6, 10, 11, 14, 15, 19, 20};
    private int[] olharDireito = {3, 4, 8, 9, 12, 13, 17, 18, 22, 23};

    public Poupador() {

        this.nome = "Poupador " + Math.floor(Math.random() * 5);

        OlhandoPara.put("CIMA", olharCima);
        OlhandoPara.put("BAIXO", olharBaixo);
        OlhandoPara.put("ESQUERDA", olharEsquerda);
        OlhandoPara.put("RIGHT", olharDireito);

        MapeadorFunção.put(0, new int[] {-2, -2});
        MapeadorFunção.put(1, new int[] {-1, -2});
        MapeadorFunção.put(2, new int[] {0, -2});
        MapeadorFunção.put(3, new int[] {1, -2});
        MapeadorFunção.put(4, new int[] {2, -2});
        MapeadorFunção.put(5, new int[] {-2, -1});
        MapeadorFunção.put(6, new int[] {-1, -1});
        MapeadorFunção.put(7, new int[] {1, 1});
        MapeadorFunção.put(8, new int[] {1, -1});
        MapeadorFunção.put(9, new int[] {2, -1});
        MapeadorFunção.put(10, new int[] {-2, 0});
        MapeadorFunção.put(11, new int[] {-1, 0});
        MapeadorFunção.put(12, new int[] {1, 0});
        MapeadorFunção.put(13, new int[] {2, 0});
        MapeadorFunção.put(14, new int[] {-2, 1});
        MapeadorFunção.put(15, new int[] {-1, 1});
        MapeadorFunção.put(16, new int[] {0, -1});
        MapeadorFunção.put(17, new int[] {1, 1});
        MapeadorFunção.put(18, new int[] {2, 1});
        MapeadorFunção.put(19, new int[] {-2, 2});
        MapeadorFunção.put(20, new int[] {-1, 2});
        MapeadorFunção.put(21, new int[] {0, 2});
        MapeadorFunção.put(22, new int[] {1, 2});
        MapeadorFunção.put(23, new int[] {2, 2});

        VontadeIrBanco = pesoInicio();
        irPraLonge = pesoInicio();

        metaFinal("teste");
    }

    public int acao() {
        peso = new int[24];
        int move = acaoFinal();
        return move;
    }
    

    public int acaoFinal() {
    	verificarSentidos();
    	int action = verificarAcao();
    	return action;
    }
    
    public void tempoSemPegarMoeda(int moedaAtual) {
    	int moedasTotais = sensor.getNumeroDeMoedas();
    	
    	if (moedaAtual == moedasTotais) {
    		tempoSemMoeda++;
    	} else {
    		irProBanco = (int) Math.pow(moedasTotais, 2);
    		tempoSemMoeda = 0;
    	}
    }

    public void verificarMapa() {
        // Ajusta o peso da ação anterior
        if (acaoAnterior != 0) {
            peso[acaoAnterior] -= 50;
        }
    
        // Obtém a posição atual e atualiza o mapa do labirinto
        Point posAtual = sensor.getPosicao();
        mapaDoLabirinto.merge(posAtual, 1, Integer::sum);
    
        // Atualiza pesos com base na visão atual
        int[] visaoAtual = sensor.getVisaoIdentificacao();
        for (int i = 0; i < visaoAtual.length; i++) {
            Point noMapa = getLocalPontos(i);
            int fatorPeso = mapaDoLabirinto.getOrDefault(noMapa, 0);
            peso[i] -= 5 * fatorPeso;
            System.out.println("funcionou finalmente");
        }
    }
    
    
    public int vontadeIrBanco() {
        int moedasTotais = sensor.getNumeroDeMoedas();
        if (moedasTotais < 1) {
            return 0; // Retorna 0 se não houver moedas
        }
        
        int localizacaoDoBanco = getPosicaoBanco() ? 1 : 0;
        return 1000 * localizacaoDoBanco * moedasTotais;
    }
    
    
    public int verificaMoeda() {
        int testando = 0;
        int localizacaoDoBanco = getPosicaoBanco() ? 1 : -1;
        int surroundingThiefAmountVision = vendoLadrao();
    
        switch (getGoal()) {
            case "teste":
                testando = calcularTeste(localizacaoDoBanco);
                break;
            case "get coins":
                testando = calcularGetCoins(localizacaoDoBanco);
                break;
            default:
                break;
        }
    
        // Ajusta o valor de testando se houver ladrões na visão
        if (surroundingThiefAmountVision > 0) {
            testando /= surroundingThiefAmountVision;
        }
    
        return testando;
    }
    
    private int calcularTeste(int localizacaoDoBanco) {
        return (int) (50 * localizacaoDoBanco * VontadeIrBanco) + tempoSemMoeda;
    }
    
    private int calcularGetCoins(int localizacaoDoBanco) {
        return (int) (100 * localizacaoDoBanco * (1 - irPraLonge));
    }
    
    
    public void deveIrBanco() {
        if (LOCAL_BANCO.equals(new Point(-4, -4)) || !getPosicaoBanco()) {
            return; // Sai da função se LOCAL_BANCO é (-4, -4) ou a posição do banco não é válida
        }
    
        irPraLonge = (irPraLonge == 1) ? 0.9999999999999999 : irPraLonge; // Atualiza irPraLonge, se necessário
        double distanciaTotal = (irProBanco / (1 - irPraLonge)) + calculoDaDistancia(LOCAL_BANCO);
        System.out.println(distanciaTotal);
    }
    
  
    
    public void verificarSentidos() {
    	deveIrBanco();
    }
    
    public int verificarAcao() {
        int action = 0;
        int[] visaoAtual = sensor.getVisaoIdentificacao();
        verificarMapa();
        
        processarVisao(visaoAtual);
        processarOlhandoPara(visaoAtual);
        
        action = determinarAcaoFinal();
        
        acaoAnterior = getAcaoOposta(action);
        atualizarContagemMoeda();
        
        
        return action;
    }
    
    private void processarVisao(int[] visaoAtual) {
        for (int i = 0; i < visaoAtual.length; i++) {
            Point noMapa = getLocalPontos(i);
            int estado = visaoAtual[i];
    
            processarEstado(estado, i);
        }
    }
    
    private void processarEstado(int estado, int indice) {
        if (estado == BANCO) {
            tratarBanco(indice);
        } else if (estado == MOEDA) {
            adicionarPesoMoeda(indice);
        } else {
            tratarEstadoEspecial(estado, indice);
        }
    }
    
    private void adicionarPesoMoeda(int indice) {
        peso[indice] += verificaMoeda();
    }
    
    private void tratarBanco(int indice) {
        if (!getPosicaoBanco()) {
            atualizarLocalBanco(indice);
        }
        peso[indice] += vontadeIrBanco();
    }
    
    private void atualizarLocalBanco(int indice) {
        LOCAL_BANCO = getLocalPontos(indice);
        setSabePosBanco(true);
        metaFinal("get coins");
    }
    
    
    private void tratarEstadoEspecial(int estado, int i) {
        if (isEstadoBanco(estado)) {
            if (isBancoDesconhecido()) {
                setSabePosBanco(true);
                metaFinal("get coins");
            }
            peso[i] -= 200;
        } else if (isLadraoEncontrado(estado)) {
            System.out.println("Ladrao Found");
            peso[i] -= 500;
        }
    }
    
    private boolean isEstadoBanco(int estado) {
        return estado >= 100 && estado < 200;
    }
    
    private boolean isBancoDesconhecido() {
        return !getPosicaoBanco() && !LOCAL_BANCO.equals(new Point(-4, -4));
    }
    
    private boolean isLadraoEncontrado(int estado) {
        return estado >= 200;
    }
    
    private void processarOlhandoPara(int[] visaoAtual) {
        OlhandoPara.forEach((key, value) -> {
            int pos = VisaoPoupador.valueOf(key).getValue();
            int movePos = visaoAtual[pos];
    
            System.out.print(key + ": " + movePos + " ");
            ajustarPesoOlhandoPara(movePos, pos);
        });
    }
    
    
    private void ajustarPesoOlhandoPara(int movePos, int pos) {
        if (movePos == PAREDE || movePos == PRA_FORA || movePos >= 100) {
            peso[pos] -= 1500;
        } else if (movePos == PASTILAH_PODER && sensor.getNumeroDeMoedas() < 5) {
            peso[pos] -= 500;
        }
    }
    
    private int determinarAcaoFinal() {
        Integer[] diretionWeights = {
            getSomaCustos("CIMA"), 
            getSomaCustos("BAIXO"), 
            getSomaCustos("RIGHT"), 
            getSomaCustos("ESQUERDA")
        };
        
        int maxx = Collections.max(Arrays.asList(diretionWeights));
        List<Integer> equalWeights = new ArrayList<>();
    
        for (int i = 0; i < diretionWeights.length; i++) {
            if (diretionWeights[i] == maxx) {
                equalWeights.add(i + 1);
            }
        }
    
        if (equalWeights.size() > 1) {
            int indice = (int) (Math.random() * equalWeights.size());
            return equalWeights.get(indice);
        }
        
        return pegarCustoMax();
    }
    
    private void atualizarContagemMoeda() {
        tempoSemPegarMoeda(contagemMoeda);
        contagemMoeda = sensor.getNumeroDeMoedas();
    }
    
    
    private int vendoLadrao() {
    	int[] vision = sensor.getVisaoIdentificacao();
        int thiefNum = 0;
        for(int i = 0; i< vision.length; i++) {
            if(vision[i] >= BIXO_FEI) {
                thiefNum++;
            }
        }

        return thiefNum;
    }
    
    public int calculoDaDistancia(Point finish) {
    	Point start = sensor.getPosicao();
        return Math.abs(start.x - finish.x) + Math.abs(start.y - finish.y);
    }
    
    public int getSomaCustos(String move) {
        int[] moveSpotsInVisionArray = OlhandoPara.get(move);
        int totalSum = 0;
        for(int testando: moveSpotsInVisionArray) {
            totalSum += peso[testando];
        }
        
        return totalSum;
    }
    
    public int getAcaoOposta(int move) {
        if (move == 1) {
            return 16;
        } else if (move == 2) {
            return 7;
        } else if (move == 4) {
            return 12;
        } else if (move == 3) {
            return 11;
        }

        return 0;
    }

    public int pegarCustoMax() {
        int maximo = Integer.MIN_VALUE; // Utiliza o valor mínimo possível para inteiros
        String move = "";
    
        for (String key : OlhandoPara.keySet()) {
            int custoAtual = getSomaCustos(key); // Calcula uma vez o custo
            if (custoAtual > maximo) {
                maximo = custoAtual;
                move = key;
            }
        }
    
        return MapaPoupador.valueOf(move).getValue();
    }
    

    public Point getProxPos(String move) {
        Point posAtual = sensor.getPosicao();
        Point proxPos = posAtual;
        switch (move) {
            case "CIMA":
            proxPos.y--;
            break;
            case "BAIXO":
            proxPos.y++;
            break;
            case "RIGHT":
            proxPos.x++;
            break;
            case "ESQUERDA":
            proxPos.x--;
            break;
        }
        
        return proxPos;
    }

    public Point getLocalPontos(int visionPos) {
        Point currentP = sensor.getPosicao();
        int[] adjustments = MapeadorFunção.get(visionPos);
        int adjustedX = currentP.x + adjustments[0];
        int adjustedY = currentP.y + adjustments[1];
    
        if (comLimite(adjustedX, adjustedY)) {
            return new Point(adjustedX, adjustedY);
        }
    
        return new Point(-1, -1);
    }
    
    private boolean comLimite(int x, int y) {
        return x >= 0 && x < 30 && y >= 0 && y < 30;
    }


    private double pesoInicio() {
        return new Random().nextDouble();
    }

    public void setSabePosBanco(boolean localizacaoDoBanco) {
        this.localizacaoDoBanco = localizacaoDoBanco;
    }

    public boolean getPosicaoBanco() {
        return localizacaoDoBanco;
    }

    public String getGoal() {
        return meta;
    }

    public void metaFinal(String meta) {
        this.meta = meta;
    }
}