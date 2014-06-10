package app.caronacomunitaria.br.map;

import java.util.ArrayList;

import app.caronacomunitaria.br.ui.PontoMapa;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

public class Coordenadas {
	
	public Coordenadas()
	{
		carregarCoordenadas();		
		carregarTitulos();
	}
	
	private static ArrayList<PontoMapa> coordenadas = new ArrayList<PontoMapa>();
	private static ArrayList<String> titulos = new ArrayList<String>();
 

	
	LatLng METRO = new LatLng(-23.571611, -46.708702);
	LatLng AFRANIO = new LatLng(-23.566723, -46.709076);
	LatLng ED_FISICA = new LatLng(-23.563593, -46.712789);
	LatLng CPTM_IDA = new LatLng(-23.560494, -46.713150);
	LatLng RAIA = new LatLng(-23.556244, -46.721699);
	LatLng PSICO = new LatLng(-23.554483, -46.724746);
	LatLng P2 = new LatLng(-23.552742, -46.728168);
	LatLng TERMINAL = new LatLng(-23.552369, -46.731827);
	LatLng IPT = new LatLng(-23.555781, -46.733640);
	LatLng ELETROTECNICA = new LatLng(-23.557984, -46.732685);
	LatLng FAU = new LatLng(-23.559351, -46.729890);
	LatLng GEOCIENCIAS = new LatLng(-23.560652, -46.727188);
	LatLng LETRAS = new LatLng(-23.562060, -46.724405);
	LatLng HIST = new LatLng(-23.564056, -46.722538);
	LatLng FARMA = new LatLng(-23.565305, -46.724916);
	LatLng BIBLIO_FARMA = new LatLng(-23.566495, -46.727041);
	LatLng LAGO = new LatLng(-23.567493, -46.728938);
	LatLng P3 = new LatLng(-23.568266, -46.740530);
	LatLng INDIANA = new LatLng(-23.568478, -46.731642);
	LatLng BIOMED = new LatLng(-23.568267, -46.731921);
	LatLng IPEN = new LatLng(-23.566071, -46.738497);
	LatLng COPESP = new LatLng(-23.563839, -46.740760);
	LatLng RIO_PEQUENO = new LatLng(-23.559836, -46.741823);
	LatLng PREFEITURA = new LatLng(-23.559807, -46.739419);
	LatLng FISICA = new LatLng(-23.559679, -46.734838);
	LatLng OCEANOGRAFIA = new LatLng(-23.560909, -46.730647);
	LatLng BIOCIENCIAS = new LatLng(-23.566158, -46.730417);
	LatLng CEPAN = new LatLng(-23.565932, -46.725621);
	LatLng BUTANTA = new LatLng(-23.564135, -46.722215);
	LatLng CULTURA_JAP = new LatLng(-23.562925, -46.719747);
	LatLng PACO = new LatLng(-23.563613, -46.716282);
	LatLng ACAD_POLICIA = new LatLng(-23.565447, -46.713335);
	LatLng VITAL = new LatLng(-23.570971, -46.709612);


	LatLng AFRANIO2 = new LatLng(-23.566723, -46.709076);
	LatLng EDUCACAO = new LatLng(-23.563284, -46.716054);
	LatLng REITORIA = new LatLng(-23.560501, -46.720185);
	LatLng BIBLIOTECA = new LatLng(-23.562222, -46.723296);
	LatLng BANCOS = new LatLng(-23.560125, -46.727266);
	LatLng FEA = new LatLng(-23.558945, -46.729841);
	LatLng BIENIO = new LatLng(-23.557834, -46.732287);
	LatLng PREFEITURA2 = new LatLng(-23.558965, -46.737780);
	LatLng MAE = new LatLng(-23.559719, -46.742198);
	LatLng HU = new LatLng(-23.563770, -46.741414);
	LatLng BIOMEDICAS_III = new LatLng(-23.564724, -46.739687);
	LatLng ODONTO = new LatLng(-23.566681, -46.738400);
	LatLng P3_2 = new LatLng(-23.568266, -46.740530);
	LatLng INDIANA2 = new LatLng(-23.568478, -46.731642);
	LatLng BIOCIENCIAS2 = new LatLng(-23.566158, -46.730417);
	LatLng FILOSOFIA = new LatLng(-23.561403, -46.730078);
	LatLng FAU2 = new LatLng(-23.560677, -46.730986);
	LatLng IAG = new LatLng(-23.559635, -46.734516);
	LatLng CLUBE_FUNCIONARIO = new LatLng(-23.559487, -46.737681);
	LatLng CIVIL = new LatLng(-23.555761, -46.733609);
	LatLng METALURGIA = new LatLng(-23.553037, -46.732014);
	LatLng MECANICA = new LatLng(-23.552846, -46.728735);
	LatLng HIDRAULICA = new LatLng(-23.556157, -46.726295);
	LatLng BARRACOES = new LatLng(-23.557837, -46.727252);
	LatLng ECA = new LatLng(-23.557830, -46.726905);
	LatLng PSICO_I = new LatLng(-23.556093, -46.725955);
	LatLng RELOGIO = new LatLng(-23.555307, -46.724033);
	LatLng CRUSP = new LatLng(-23.557279, -46.719972);
	LatLng CPTM = new LatLng(-23.560870, -46.713285);
	LatLng ED_FISICA2 = new LatLng(-23.563706, -46.713066);
	LatLng ACAD_POLICIA2 = new LatLng(-23.565447, -46.713335);
	LatLng VITAL2 = new LatLng(-23.570971, -46.709612);



	//FIXME alguns nomes estao errados
	
	public void carregarCoordenadas(){
	coordenadas.add(new PontoMapa(METRO, "METRO", "Metro Butantã",
			BitmapDescriptorFactory.HUE_BLUE));
	coordenadas.add(new PontoMapa(AFRANIO, "AFRANIO", "Av. Afranio Peixoto",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(ED_FISICA, "ED FÍSICA", "Educacao Física",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(CPTM_IDA, "CPTM IDA", "CPTM",
			BitmapDescriptorFactory.HUE_BLUE));
	coordenadas.add(new PontoMapa(RAIA, "RAIA", "Raia",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(PSICO, "PSICO", "Psicologia",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(P2, "P2", "P2", BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(TERMINAL, "TERMINAL", "Terminal USP",
			BitmapDescriptorFactory.HUE_BLUE));
	coordenadas.add(new PontoMapa(IPT, "IPT", "Instituto de Pesquisas Tecnológicas",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(ELETROTECNICA, "ELETROTÉCNICA", "Eletrotécnica",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(FAU, "FAU", "Faculdade de Arquitetura e Urbanismo",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(GEOCIENCIAS, "GEOCIÊNCIAS", "Geociências",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(LETRAS, "LETRAS", "Letras",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(HIST, "HIST", "Historia e Geografia",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(FARMA, "FARMA", "Farmácia e Química",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(BIBLIO_FARMA, "BIBLIO FARMA",
			"Biblioteca Farmacia e Química",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(LAGO, "LAGO", "Rua do Lago",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(P3, "P3", "P3", BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(INDIANA2, "INDIANA", "Vila Indiana",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(BIOMED, "BIOMED", "Biomédicas",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(IPEN, "IPEN",
			"Instituto de Pesquisas Energéticas e Nucleares",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(COPESP, "COPESP", "COPESP",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(RIO_PEQUENO, "RIO_PEQUENO", "Rio Pequeno",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(PREFEITURA, "PREFEITURA", "Prefeitura",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(FISICA, "FÍSICA", "Instituto de Física",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(OCEANOGRAFIA, "OCEANOGRAFIA", "Oceanografia",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(BIOCIENCIAS, "BIOCIÊNCIAS", "Biociências",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(CEPAN, "CEPAN",
			"Centro de Estudos e Pesquisas de Administração Municipal",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(BUTANTA, "BUTANTÃ", "Instituto Butantã",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(CULTURA_JAP, "CULTURA JAP", "Cultura Japonesa",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(PACO, "PACO", "Paço das Artes",
			BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(ACAD_POLICIA, "ACAD POLICIA",
			"Academia de Policia", BitmapDescriptorFactory.HUE_RED));
	coordenadas.add(new PontoMapa(VITAL, "VITAL", "Av. Vital Brasil",
			BitmapDescriptorFactory.HUE_RED));

	coordenadas.add(new PontoMapa(AFRANIO2, "AFRANIO", "Av. Afranio Peixoto",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(EDUCACAO, "EDUCACÃO", "Faculdade de Educação",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(REITORIA, "REITORIA", "Reitoria",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(BIBLIOTECA, "BIBLIOTECA", "Biblioteca",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(BANCOS, "BANCOS", "Bancos",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(FEA, "FEA",
			"Faculdade de Economia e Administrão",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(BIENIO, "BIENIO", "Poli - Biênio",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(PREFEITURA2, "PREFEITURA", "Prefeitura",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(MAE, "MAE", "Museu de Arqueologia e Etnologia",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(HU, "HU", "Hospital Universitário",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(BIOMEDICAS_III, "BIOMEDICAS_III", "Biomédicas",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(ODONTO, "ODONTO", "Faculdade de Odontologia",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(P3_2, "P3", "P3", BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(INDIANA, "INDIANA", "Vila Indiana",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(BIOCIENCIAS2, "BIOCIÊNCIAS", "Biociências",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(FILOSOFIA, "FILOSOFIA", "Filosofia",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(FAU2, "FAU",
			"Faculdade de Arquitetura e Urbanismo",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(IAG, "IAG", "Instituto de Astronomia e Geofísica",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(CLUBE_FUNCIONARIO, "CLUBE_FUNCIONÁRIO",
			"Clube dos Funcionários", BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(CIVIL, "CIVIL", "Poli - Civil",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(METALURGIA, "METALURGIA", "Poli - Metalurgia",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(MECANICA, "MECÂNICA", "Poli - Mecânica",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(HIDRAULICA, "HIDRÁULICA", "Poli - Hidráulica",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(BARRACOES, "BARRACOES", "Barrações",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(ECA, "ECA", "Escola de Comunicações e Artes",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(PSICO_I, "PSICO_I", "Psicologia",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(RELOGIO, "RELOGIO", "Praça do Relógio",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(CRUSP, "CRUSP", "CRUSP",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(CPTM, "CPTMVOLTA", "CPTM",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(ED_FISICA2, "ED FÍSICA", "Educação Física",
			BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(ACAD_POLICIA2, "ACAD POLICIA",
			"Academia de Polícia", BitmapDescriptorFactory.HUE_GREEN));
	coordenadas.add(new PontoMapa(VITAL2, "VITAL", "Av. Vital Brasil",
			BitmapDescriptorFactory.HUE_GREEN));
	
	return;
	
	}
	
	public void carregarTitulos()
	{
		for (PontoMapa ponto : coordenadas) {			
			titulos.add(ponto.getTitulo());
		}
	}
	
	public  ArrayList<PontoMapa> getCoordenadas(){
		return coordenadas;
	}
	
	public  ArrayList<String> getTitulos(){
		return titulos;
	}
}
