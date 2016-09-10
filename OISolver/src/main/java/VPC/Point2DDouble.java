/*
 * Created on Apr 25, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package VPC;

import java.awt.geom.Point2D;
import java.io.Serializable;
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


/**
 * @author padeler 
 */
public class Point2DDouble extends Point2D implements Serializable
{
	public double x, y;

	public Point2DDouble(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public Point2DDouble()
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.geom.Point2D#getX()
	 */
	public double getX()
	{
		return x;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.geom.Point2D#getY()
	 */
	public double getY()
	{
		return y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.geom.Point2D#setLocation(double, double)
	 */
	public void setLocation(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public double distanceSQ(Point2DDouble p)
	{
		return (((p.x - x) * (p.x - x)) + ((p.y - y) * (p.y - y)));
	}

	public double distance(Point2DDouble p)
	{
		return (Math.sqrt(this.distanceSQ(p)));
	}

	/**
	 * @param d
	 */
	public void setX(double d)
	{
		x = d;
	}

	/**
	 * @param d
	 */
	public void setY(double d)
	{
		y = d;
	}

	public String toString()
	{
		return (new String("[" + x + " x " + y + "]"));
	}

}
