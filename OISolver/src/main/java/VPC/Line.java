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

package VPC;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * @author padeler
 */
public class Line implements Serializable
{
	public Point2D p0 = null;// simis pano stin eikona gia anaparastasi tou
								// efthigramou tmimatos

	public Point2D p1 = null;// pou edose tin efthia.

	private Point2D distantP0 = null; // Simia se megali apostasi gia
										// anaparastasi tis grammhs

	private Point2D distantP1 = null;

	private static boolean translate = false;

	private static int width = 1000;

	private static int height = 1000;

	public double A = 0;

	public double B = 0;

	public double C = 0;

	public Line(double x0, double y0, double x1, double y1)
	{
		this.p0 = new Point2DDouble(x0, y0);
		this.p1 = new Point2DDouble(x1, y1);
		A = y1 - y0;
		B = x0 - x1;
		C = ((-A) * x0) - (B * y0);

		if (B == 0)
		{
			distantP0 = new Point2DDouble(-C / A, -height);
			distantP1 = new Point2DDouble(-C / A, height);
		}
		else if (A == 0)
		{
			distantP0 = new Point2DDouble(-width, -C / B);
			distantP1 = new Point2DDouble(width, -C / B);
		}
		else
		{
			distantP0 = new Point2DDouble(-width, -((A * (-width) + C) / B));
			distantP1 = new Point2DDouble(width, -((A * width + C) / B));
		}

	}

	public Line(double A, double B, double C)
	{
		this.A = A;
		this.B = B;
		this.C = C;

		if (B == 0)
		{
			p0 = new Point2DDouble(-C / A, -height);
			p1 = new Point2DDouble(-C / A, height);
		}
		else if (A == 0)
		{
			p0 = new Point2DDouble(-width, -C / B);
			p1 = new Point2DDouble(width, -C / B);
		}
		else
		{
			p0 = new Point2DDouble(-width, -((A * (-width) + C) / B));
			p1 = new Point2DDouble(width, -((A * width + C) / B));
		}
		distantP0 = p0;
		distantP1 = p1;

		if (translate == true)
		{
			double x, y;
			x = p0.getX() + width;
			y = p0.getY() + height;
			p0.setLocation(x, y);

			x = p1.getX() + width;
			y = p1.getY() + height;
			p1.setLocation(x, y);
		}

	}

	public Line(double A, double B, double C, Point sp, Point ep)
	{
		this(A, B, C, new Point2DDouble(sp.x, sp.y), new Point2DDouble(ep.x, ep.y));
	}

	public Line(double A, double B, double C, Point2DDouble sp, Point2DDouble ep)
	{
		this.A = A;
		this.B = B;
		this.C = C;

		if (B == 0)
		{
			p0 = new Point2DDouble(-C / A, sp.y);
			p1 = new Point2DDouble(-C / A, ep.y);
		}
		else if (A == 0)
		{
			p0 = new Point2DDouble(sp.x, -C / B);
			p1 = new Point2DDouble(ep.x, -C / B);
		}
		else
		{
			double x = sp.x;
			p0 = new Point2DDouble(x, -((A * (x) + C) / B));
			x = ep.x;
			p1 = new Point2DDouble(x, -((A * x + C) / B));
		}

		Line l = Line.calculatePointsAt(this, width, height);
		this.distantP0 = l.p0;
		this.distantP1 = l.p1;

		if (translate == true)
		{
			double x, y;
			x = p0.getX() + width;
			y = p0.getY() + height;
			p0.setLocation(x, y);

			x = p1.getX() + width;
			y = p1.getY() + height;
			p1.setLocation(x, y);

		}
	}

	public void setLine(double A, double B, double C)
	{
		this.A = A;
		this.B = B;
		this.C = C;

		if (B == 0)
		{
			p0 = new Point2DDouble(-C / A, -height);
			p1 = new Point2DDouble(-C / A, height);
		}
		else if (A == 0)
		{
			p0 = new Point2DDouble(-width, -C / B);
			p1 = new Point2DDouble(width, -C / B);
		}
		else
		{
			p0 = new Point2DDouble(-width, -((A * (-width) + C) / B));
			p1 = new Point2DDouble(width, -((A * width + C) / B));
		}

		distantP0 = p0;
		distantP1 = p1;

		if (translate == true)
		{
			double x, y;
			x = p0.getX() + width;
			y = p0.getY() + height;
			p0.setLocation(x, y);

			x = p1.getX() + width;
			y = p1.getY() + height;
			p1.setLocation(x, y);
		}
	}

	public static Line calculatePointsAt(Line l, int w, int h)
	{
		double A = l.A;
		double B = l.B;
		double C = l.C;
		Point2DDouble p0, p1;

		if (B == 0)
		{
			p0 = new Point2DDouble(-C / A, -h);
			p1 = new Point2DDouble(-C / A, h);
		}
		else if (A == 0)
		{
			p0 = new Point2DDouble(-w, -C / B);
			p1 = new Point2DDouble(w, -C / B);
		}
		else
		{
			p0 = new Point2DDouble(-w, -((A * (-w) + C) / B));
			p1 = new Point2DDouble(w, -((A * w + C) / B));
		}

		Line res = new Line();
		res.p0 = p0;
		res.p1 = p1;

		return res;
	}

	public Line()
	{

	}

	public Line(Point2DDouble p0, Point2DDouble p1)
	{
		A = p1.y - p0.y;
		B = p0.x - p1.x;
		C = ((-A) * p0.x) - (B * p0.y);
		this.p0 = p0;
		this.p1 = p1;

		Line l = Line.calculatePointsAt(this, width, height);
		this.distantP0 = l.p0;
		this.distantP1 = l.p1;

	}

	public Point2DDouble getP0()
	{
		return (Point2DDouble) (p0);
	}

	public Point2DDouble getP1()
	{
		return (Point2DDouble) (p1);
	}

	public String toString()
	{
		return (new String("(" + (long) p0.getX() + "," + (long) p0.getY() + ")-(" + (long) p1.getX() + "," + (long) p1.getY() + ")"));
	}

	public boolean addPoint(Point2DDouble p)
	{
		if (p0 == null)
		{
			p0 = p;
			return false;
		}
		else
		{
			p1 = p;
			A = p1.getY() - p0.getY();
			B = p0.getX() - p1.getX();
			C = ((-A) * p0.getX()) - (B * p0.getY());
			return true;
		}
	}

	/**
	 * @return
	 */
	public static int getHeight()
	{
		return height;
	}

	/**
	 * @return
	 */
	public static boolean isTranslate()
	{
		return translate;
	}

	/**
	 * @return
	 */
	public static int getWidth()
	{
		return width;
	}

	/**
	 * @param i
	 */
	public static void setHeight(int i)
	{
		System.out.println("Height set");
		System.exit(1);

		height = i;
	}

	/**
	 * @param b
	 */
	public static void setTranslate(boolean b)
	{
		translate = b;
	}

	/**
	 * @param i
	 */
	public static void setWidth(int i)
	{
		System.out.println("Width set");
		System.exit(1);
		width = i;
	}

	/**
	 * @return
	 */
	public Point2DDouble getDistantP0()
	{
		return (Point2DDouble) distantP0;
	}

	/**
	 * @return
	 */
	public Point2DDouble getDistantP1()
	{
		return (Point2DDouble) distantP1;
	}

}