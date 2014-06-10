package app.caronacomunitaria.br.ui;

import com.google.android.gms.maps.model.LatLng;

public class PontoMapa {
	private LatLng coordenada;
	private String titulo;
	private String descricao;
	private float cor;


	public PontoMapa(LatLng coordenada, String titulo, String descricao, float cor)
	{
		this.coordenada = coordenada;
		this.titulo = titulo;
		this.descricao = descricao;
		this.cor = cor;
	}
	/**
	 * @return the coordenada
	 */
	public LatLng getCoordenada() {
		return coordenada;
	}
	/**
	 * @param coordenada the coordenada to set
	 */
	public void setCoordenada(LatLng coordenada) {
		this.coordenada = coordenada;
	}
	/**
	 * @return the titulo
	 */
	public String getTitulo() {
		return titulo;
	}
	/**
	 * @param titulo the titulo to set
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}
	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	/**
	 * @return the cor
	 */
	public float getCor() {
		return cor;
	}
	/**
	 * @param cor the cor to set
	 */
	public void setCor(float cor) {
		this.cor = cor;
	}



}
