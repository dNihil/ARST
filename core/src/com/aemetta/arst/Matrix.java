package com.aemetta.arst;

import java.util.Arrays;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Matrix {
	
	final int WIDTH;
	final int HEIGHT;
	final int TOP;
	boolean[][] solid;
	int[][] color;
	Wang[][] wang;
	int[][] texture;
	boolean[][] updated;
	Sprite[][] sprite;
		//ALL ARRAYS WITH X/Y VALUES FOR BLOCKS ON THE matrix
		//ARE IN THE FORM var[y][x] STARTING WITH Y
	
	public Matrix(){
		this(10, 24, 4);
	}
	
	public Matrix(int w, int h) {
		this(w, h+4, 4);
	}
	
	public Matrix(int w, int h, int t) {
		WIDTH = w;
		HEIGHT = h;
		TOP = t;
		
		solid = new boolean[HEIGHT][WIDTH];
		color = new int[HEIGHT][WIDTH];
		wang = new Wang[HEIGHT][WIDTH];
		texture = new int[HEIGHT][WIDTH];
		updated = new boolean[HEIGHT][WIDTH];
		sprite = new Sprite[HEIGHT][WIDTH];
	}
	
	public int consolidate(){
		int cleared = 0;
		for(int i = HEIGHT-1; i >= 0; i--){
			boolean c = true;
			for(int j = 0; j < WIDTH; j++)
				if(!solid[i][j]) c = false;
			if(c){
				cleared++;
				shift(i, false);
				for(int j = 0; j < WIDTH; j++){
					if(solid[i][j])
						wang[i][j] = wang[i][j].shear(true);
					if(i > 0)
						if(solid[i-1][j]) {
							wang[i-1][j] = wang[i-1][j].shear(false);
							updated[i-1][j] = true;
						}
				}
			}
		}
		return cleared;
	}
	
	public void shift(int line, boolean up){
		if(!up){ //Shift everything down, clearing lines
			for(int i = line; i < HEIGHT-1; i++){
				solid[i] = solid[i+1];
				color[i] = color[i+1];
				wang[i] = wang[i+1];
				for(int j = 0; j < WIDTH; j++)
					updated[i][j] = true;
			}
			solid[HEIGHT-1] = new boolean[WIDTH];
			color[HEIGHT-1] = new int[WIDTH];
			wang[HEIGHT-1] = new Wang[WIDTH];
			updated[HEIGHT-1] = new boolean[WIDTH];
			Arrays.fill(updated[HEIGHT-1], true);
		}
		else{ //shift everything up, adding garbage
			for(int i = HEIGHT-1; i > line; i--){
				solid[i] = solid[i-1];
				color[i] = color[i-1];
				wang[i] = wang[i-1];
				for(int j = 0; j < WIDTH; j++)
					updated[i][j] = true;
			}
			solid[line] = new boolean[WIDTH];
			color[line] = new int[WIDTH];
			wang[line] = new Wang[WIDTH];
			updated[line] = new boolean[WIDTH];
			Arrays.fill(updated[HEIGHT-1], true);
		}
	}
	
	public void smooth(int start, int end){
		if(start > end){int c = start; start = end; end = c;}
		for(int i = start; i <= end; i++){
			for(int j = 0; j < WIDTH; j++){
				wang[i][j] = Wang.MIDDLE;
				if(j == 0 || color[i][j] != color[i][j-1])
					wang[i][j] = Wang.WTEE;
				if(j == WIDTH-1 || color[i][j] != color[i][j+1]){
					if(wang[i][j] == Wang.WTEE) wang[i][j] = Wang.VERT;
					else wang[i][j] = Wang.ETEE;
				}
				if(i == start || color[i][j] != color[i-1][j])
					wang[i][j] = wang[i][j].shear(true);
				if(i == end || color[i][j] != color[i+1][j])
					wang[i][j] = wang[i][j].shear(false);
				
				updated[i][j] = true;
			}
		}
	}
	
	public void setSquare(int x, int y, int c, Wang s, int t){
		setSquare(x, y, c, s);
		texture[y][x] = t;
	}
	
	public void setSquare(int x, int y, int c, Wang s){
		color[y][x] = c;
		wang[y][x] = s;
		updated[y][x] = true;
	}
	
	public void hideSquare(int x, int y){
		color[y][x] = 0;
		wang[y][x] = Wang.LONER;
	}
	
	public boolean isSolid(int x, int y){
		try{
			return solid[y][x];
		} catch(ArrayIndexOutOfBoundsException e){
			return true; }
	}
	
	public boolean hasUpdated(int x, int y) {
		if(updated[y][x]) {
			updated[y][x] = false;
			return true;
		}
		return false;
	}
	
	public void setSprite(int x, int y, Sprite s) {
		sprite[y][x] = s;
	}
	
	public Sprite getSprite(int x, int y) {
		return sprite[y][x];
	}
	
	public void makeSolid(int x, int y){
		solid[y][x] = true;;
	}
	
	public int getColor(int x, int y) {
		return color[y][x];
	}

	public Wang getShape(int x, int y) {
		return wang[y][x];
	}

	public int getTexture(int x, int y) {
		return texture[y][x];
	}
	
	public Matrix clone() {
		Matrix m = new Matrix(this.WIDTH, this.HEIGHT, this.TOP);
		m.solid = this.solid.clone();
		m.color = this.color.clone();
		m.wang = this.wang.clone();
		m.texture = this.texture.clone();
		m.updated = this.updated.clone();
		m.sprite = this.sprite.clone();
		return m;
	}
	
	public boolean isEmpty() {
		boolean r = true;
		
		for(int i = 0; i < WIDTH; i++)
			if(isSolid(i, 0)) r = false;
			
		return r;
	}
}
