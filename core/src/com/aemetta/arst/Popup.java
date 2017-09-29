package com.aemetta.arst;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Popup {
	
	TextureAtlas atlas;
	TextureRegion image;
	TextureRegion image2;
	
	boolean alive = false;
	long life = 0;
	
	public Popup() {
	}
	
	public void setAtlas(TextureAtlas a) {
		atlas = a;
	}
	
	public void create(int lines, boolean tspin, boolean btb, int combo, int height) {
		
		image = null;
		if(tspin) {
			if(btb) {
				switch(lines) {
				case 0: set(1, "tspin-btb"); break;
				case 1: set(1, "ts-single-btb"); break;
				case 2: set(1, "ts-double-btb"); break;
				case 3: set(1, "ts-triple-btb"); break;
				}
			} else {
				switch(lines) {
				case 0: set(1, "tspin"); break;
				case 1: set(1, "ts-single"); break;
				case 2: set(1, "ts-double"); break;
				case 3: set(1, "ts-triple"); break;
				}
			}
		} else {
			switch(lines) {
			case 1: set(1, "single"); break;
			case 2: set(1, "double"); break;
			case 3: set(1, "triple"); break;
			case 4: 
				if(btb)set(1, "quadra-btb");
				else set(1, "quadra");
				break;
			}
		}
		
		switch(combo) {
		case 0: image2 = null; break;
		case 1: set(2, "combo-2"); break;
		case 2: set(2, "combo-3"); break;
		case 3: set(2, "combo-4"); break;
		case 4: set(2, "combo-5"); break;
		case 5: set(2, "combo-6"); break;
		case 6: set(2, "combo-7"); break;
		case 7: set(2, "combo-8"); break;
		case 8: set(2, "combo-9"); break;
		case 9: set(2, "combo-10"); break;
		case 10: set(2, "combo-11"); break;
		case 11: set(2, "combo-12"); break;
		}
		
		if(image != null) {
			alive = true;
			life = 500;
		} else {
			alive = false;
		}
	}
	
	public void perfectClear() {
		set(1, "perfect-clear");
		alive = true;
		life = 1000;
	}
	
	public void create(boolean singleplayer, int place) {
		image = null;
		if(singleplayer)
			switch(place) {
			case 0: set(1, "end-timer"); break;
			case 1: set(1, "end-bad"); break;
			case 2: set(1, "end-neutral"); break;
			case 3: set(1, "end-good"); break;
			}
		else
			set(1, "end-place-" + place);
		
		if(image != null) {
			alive = true;
			life = 3000;
		}
	}
	
	private void set(int i, String thing) {
		if(i==1)image = atlas.findRegion(thing);
		if(i==2)image2 = atlas.findRegion(thing);
	}
	
	public void update(long delta) {
		life -= delta;
		if(life <= 0) alive = false;
	}
}
