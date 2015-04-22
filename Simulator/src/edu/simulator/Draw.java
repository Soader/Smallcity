package edu.simulator;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.opengl.Texture;

public class Draw {

	public Draw() {
		// TODO Auto-generated constructor stub
	}

	public static void Rect(float x, float y, float width, float height) {
		Rect(x, y, width, height, 0);
	}
	
	public static void Rect(float x, float y, float width, float height, float rot) {
		glPushMatrix();
		{
			glTranslatef(x, y, 0);
			glRotatef(rot, 0, 0, 1);

			glBegin(GL_QUADS);
			{
				glVertex2f(0, 0);
				glVertex2f(0, height);
				glVertex2f(width, height);
				glVertex2f(width, 0);
			}
			glEnd();
		}
		glPopMatrix();
	}
	
	public static void Rect(float x, float y, float width, float height, Texture tex) {
		glEnable(GL_TEXTURE_2D);
		tex.bind();
		glLoadIdentity();
		glPushMatrix();
		{
			glTranslatef(x, y, 0);

			glBegin(GL_QUADS);
			{
				glTexCoord2d(0, 1); glVertex2f(0, 0);           // 0 0
				glTexCoord2d(0, 0); glVertex2f(0, height);      // 0 1
				glTexCoord2d(1, 0); glVertex2f(width, height);  // 1 1
				glTexCoord2d(1, 1); glVertex2f(width, 0);       // 1 0
			}
			glEnd();
		}
		glPopMatrix();
		glDisable(GL_TEXTURE_2D);
	}
	
	public static void Rect(float x, float y, float width, float height, float rot, Texture tex) {
		glEnable(GL_TEXTURE_2D);
		tex.bind();
		glLoadIdentity();
		glPushMatrix();
		{
			glTranslatef(x+width/2, y+height/2, 0);
			//glTranslatef(5, 5, 0);
			glRotatef(rot, 0, 0, 1);
			glTranslatef(-width/2, -height/2, 0);
			//glTranslatef(x, y, 0);
			//glTranslatef(x, y, 0);
			
			glBegin(GL_QUADS);
			{
				glTexCoord2d(0, 1); glVertex2f(0, 0);
				glTexCoord2d(0, 0); glVertex2f(0, height);
				glTexCoord2d(1, 0); glVertex2f(width, height);
				glTexCoord2d(1, 1); glVertex2f(width, 0);
			}
			glEnd();
		}
		glPopMatrix();
		glDisable(GL_TEXTURE_2D);
	}
	


}
