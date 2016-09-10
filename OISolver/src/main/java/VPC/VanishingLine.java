/*
 * 
 * SVR is an implementation of the Single view 3D reconstruction technique. 
 * It allows the recreation of 3D scenes from a single image. 
 * It provides a UI that navigates the user through a number of steps from 
 * a source image to a complete 3d model of the scene
 *
 * Copyright (C) 2004  Pashalis Padeleris
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */


/*
 * Created on 19 ��� 2003
 *
 */
package VPC;

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * @author padeler
 */
public class VanishingLine implements Serializable
{
	private Point2DDouble p = null;

	private Line line = null;

	private int support = 0;

	public VanishingLine(Line l, Point2DDouble p, int support)
	{
		this.line = l;
		this.p = p;
		this.support = support;
	}

	public Point2DDouble getPoint()
	{
		return (p);

	}

	/**
	 * @return
	 */
	public Line getLine()
	{
		return line;
	}

	/**
	 * @param line
	 */
	public void setLine(Line line)
	{
		this.line = line;
	}

	/**
	 * @return
	 */
	public int getSupport()
	{
		return support;
	}

	/**
	 * @param i
	 */
	public void setSupport(int i)
	{
		support = i;
	}

	public String toString()
	{
		return ("Support [" + support + "] point [" + p + "] line [" + line + "]\n");
	}

}
