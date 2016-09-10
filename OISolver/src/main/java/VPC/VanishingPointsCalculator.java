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

import java.awt.geom.Point2D;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;
import java.util.TreeSet;
import java.util.Vector;

/**
 * @author padeler 
 */
public class VanishingPointsCalculator
{
	private static Vector lastResult = null;

	private static int threshold = 1;

	private static int thresholdSq = 1;

	public static Vector fromLines(Vector lineset, int iter)
	{
		Hashtable concurrentLinesTable = new Hashtable();
		Line l;

		// for each line in the set
		Random rnd = new Random((new Date()).getTime());

		int count = iter;
		while ((--count) > 0)
		{
			Vector ltempset = new Vector();
			ltempset.addAll(lineset);

			while (ltempset.size() > 1)
			{
				Line l1 = (Line) ltempset.remove(rnd.nextInt(ltempset.size()));
				Line l2 = (Line) ltempset.remove(rnd.nextInt(ltempset.size()));

				Point2DDouble p = intersectLines(l1, l2);

				if (p == null)// FIXME : case that lines intersect at infinity
				{

					System.out.println("Droping Line " + l2);
					lineset.remove(l2);
					continue;
				}
				Vector supportV = distancesFromPoint(p, ltempset);
				ltempset.removeAll(supportV);
				HashSet set = new HashSet();
				set.addAll(supportV);
				concurrentLinesTable.put(p, set);
			}
		}
		Enumeration keys = concurrentLinesTable.keys();
		int max = 0;
		Vector res = new Vector();

		while (keys.hasMoreElements())
		{
			Point2DDouble p = (Point2DDouble) keys.nextElement();
			HashSet set = (HashSet) concurrentLinesTable.get(p);
			res.add(new VanishingLine(null, p, set.size()));
		}
		return res;
	}

	public static Vector getMostSupportedVL(Vector vlv)
	{
		int max = 0;
		int temp = 0;
		int pos = 0;

		Vector res = new Vector();

		for (int i = 0; i < vlv.size(); ++i)
		{
			if ((temp = ((VanishingLine) vlv.elementAt(i)).getSupport()) > max)
			{
				max = temp;
			}
		}

		int thres = max; // percent * max /100;
		for (int i = 0; i < vlv.size(); ++i)
		{
			VanishingLine vl;
			if (((vl = (VanishingLine) vlv.elementAt(i)).getSupport()) >= thres)
			{
				res.add(vl);
			}
		}

		return res;
	}

	public static Vector distancesFromPoint(Point2DDouble p0, Vector lineset)
	{
		Vector supportV = new Vector();
		double d, A, B, C, T;

		for (int i = 0; i < lineset.size(); ++i)
		{
			Line l = (Line) lineset.elementAt(i);
			A = l.A;
			B = l.B;
			C = l.C;
			T = (A * p0.x + B * p0.y + C);

			d = (T * T) / (A * A + B * B);
			if (d <= thresholdSq)
			{
				supportV.add(l);
			}
		}

		return supportV;
	}

	public static int distancesFromPoint(Point2DDouble p, Vector lineVec, Vector support, int maxThres)
	{
		int res = 0;
		double d, A, B, C, T;
		double thr = maxThres * maxThres;

		Vector lines = new Vector();
		lines.addAll(lineVec);
		lines.removeAll(support);

		for (int i = 0; i < lines.size(); ++i)
		{
			Line l = (Line) lines.elementAt(i);
			A = l.A;
			B = l.B;
			C = l.C;
			T = (A * p.x + B * p.y + C);

			d = (T * T) / (A * A + B * B);
			if (d <= thr)
			{
				++res;
			}
		}

		return res;
	}

	public static Point2DDouble intersectLines(Line l1, Line l2)
	{

		// TODO : Isos na ginete poio grigoro intersection (na dw to mathworld.)
		Point2DDouble p1 = l1.getP0();
		Point2DDouble p2 = l1.getP1();
		Point2DDouble p3 = l2.getP0();
		Point2DDouble p4 = l2.getP1();

		double[][] M1 =
		{
				{
						p1.x, p1.y
				},
				{
						p2.x, p2.y
				}
		};

		double[][] M2 =
		{
				{
						p3.x, p3.y
				},
				{
						p4.x, p4.y
				}
		};

		double[][] M3 =
		{
				{
						p1.x - p2.x, p1.y - p2.y
				},
				{
						p3.x - p4.x, p3.y - p4.y
				}
		};
		double DM1 = MatrixAlgebra.determinant(M1);
		double DM2 = MatrixAlgebra.determinant(M2);
		double DM3 = MatrixAlgebra.determinant(M3);

		double[][] MX =
		{
				{
						DM1, p1.x - p2.x
				},
				{
						DM2, p3.x - p4.x
				}
		};

		double[][] MY =
		{
				{
						DM1, p1.y - p2.y
				},
				{
						DM2, p3.y - p4.y
				}
		};

		double DMX = MatrixAlgebra.determinant(MX);
		double DMY = MatrixAlgebra.determinant(MY);

		if (DM3 == 0.0)
			return null;

		double X = DMX / DM3;
		double Y = DMY / DM3;

		return (new Point2DDouble(X, Y));
	}

	/*
	 * Removes outliers from a set of points with a media least square
	 * algorithm.
	 */
	public static Vector removeOutlayers(Vector input, int iterations, Line bLine, double threshold)
	{

		Vector result = new Vector();
		Random rnd = new Random((new Date()).getTime());
		int count = iterations;
		if (count >= ((input.size() * (input.size() - 1)) / 2))
			count = input.size();

		double minMedianDistance = 10E+19;
		int pointCount = input.size();
		int t1, t2;
		Point2DDouble p1, p2, p;
		Line l;
		Line bestLine = null;
		TreeSet sortedList;
		double temp, sd;
		// First we find the minMedianDistance.
		while ((count--) > 0)
		{
			// get 2 random points from input.
			t1 = rnd.nextInt(pointCount);
			t2 = rnd.nextInt(pointCount);
			if (t1 == t2)
			{
				continue;
			}

			p1 = (Point2DDouble) input.elementAt(t1);
			p2 = (Point2DDouble) input.elementAt(t2);

			// find the line from the 2 points.
			l = new Line(p1, p2);

			// find the distance from the line for all other points.
			sortedList = new TreeSet();
			// System.out.println("Checking for line "+l);

			for (int i = 0; i < pointCount; ++i)
			{
				if (i == t1 || i == t2)
					continue;

				p = (Point2DDouble) input.elementAt(i);

				temp = l.A * p.x + l.B * p.y + l.C;
				sd = (temp * temp) / (l.A * l.A + l.B * l.B);
				sortedList.add(new Double(sd));
			}
			if (sortedList.size() > 0)
			{

				temp = ((Double) ((sortedList.toArray())[sortedList.size() / 2])).doubleValue();
			}
			else
				temp = 0;
			if (temp < minMedianDistance)
			{
				minMedianDistance = temp;
				bestLine = l;
			}
		}

		// System.out.println("MinMedianDistance is :" + minMedianDistance);
		// System.out.println("Best Line is :" + bestLine);

		if (bestLine == null) // den vrethike tpt , kati paei lathos.
			return null;

		// Secondly determine the outlayers and remove them.

		double thres = threshold * minMedianDistance;
		Line b = bestLine;
		for (int i = 0; i < input.size(); ++i)
		{
			p = (Point2DDouble) input.elementAt(i);

			temp = b.A * p.x + b.B * p.y + b.C;
			sd = (temp * temp) / (b.A * b.A + b.B * b.B);

			if (sd <= thres)
			{
				result.add(p);
			}

		}

		System.out.println("Found " + (input.size() - result.size()) + " outlayers");

		bLine.setLine(bestLine.A, bestLine.B, bestLine.C);
		return result;
	}

	public static Vector getSupportPoints(Line medianLine, Line tl, Vector points, int width, int height)
	{
		double mA, mB, mC;
		mA = medianLine.A;
		mB = medianLine.B;
		mC = medianLine.C;

		Point2DDouble v1, v2;
		double x1, y1, x2, y2, x3, y3;

		// first get the orientation of medianLine and tl.
		if (mB != 0.0) // lines are not parallel to y'y
		{
			x1 = width / 2;
			y1 = -(mA * x1 + mC) / mB;

			x2 = x1 + 10;
			y2 = -(mA * x2 + mC) / mB;
		}
		else
		// parallel to y'y
		{
			x1 = -(mC / mA);
			y1 = height / 2;

			x2 = x1;
			y2 = y1 + 10;
		}

		if (tl.B != 0.0)
		{
			x3 = (width / 2) - 1;
			y3 = -(tl.A * x3 + tl.C) / tl.B;
		}
		else
		{
			y3 = height / 2;
			x3 = -(tl.C / tl.A);
		}

		v1 = new Point2DDouble(x2 - x1, y2 - y1);
		v2 = new Point2DDouble(x3 - x1, y3 - y1);

		double V1norm = Math.sqrt(v1.x * v1.x + v1.y * v1.y);
		double V2norm = Math.sqrt(v2.x * v2.x + v2.y * v2.y);

		double sinA1 = (v1.y) / V1norm;
		double sinA2 = (v2.y) / V2norm;
		System.out.println("sinA1 = " + sinA1 + ", sinA2 = " + sinA2);
		double A = Math.asin(sinA2) - Math.asin(sinA1);
		double sinA = Math.sin(A);
		System.out.println("sinA is [" + sinA + "]");

		Vector result = new Vector();
		// gia kathe simio chekaroume an einai stin sosth plevra. sosth plevra =
		// antitheti apo aftin pou einai h tl.
		for (int i = 0; i < points.size(); ++i)
		{
			Point2DDouble p = (Point2DDouble) points.elementAt(i);
			v2 = new Point2DDouble(p.x - x1, p.y - y1);
			V2norm = Math.sqrt(v2.x * v2.x + v2.y * v2.y);

			double sinT = (v2.y) / V2norm;
			double F = Math.asin(sinT) - Math.asin(sinA1);
			double sinF = Math.sin(F);
			if ((sinA * sinF) < 0.0)
			{
				result.add(p);
			}
		}
		return result;
	}

	public static Vector getVPCandidates(Line medianLine, Line tl, Vector points, int width, int height)
	{
		double mA, mB, mC;
		mA = medianLine.A;
		mB = medianLine.B;
		mC = medianLine.C;
		double tlA, tlB, tlC;
		tlA = tl.A;
		tlB = tl.B;
		tlC = tl.C;

		Point2DDouble v1, v2;
		double x1, y1, x2, y2, x3, y3;

		// first get the orientation of medianLine and tl.
		if (tlB != 0.0) // lines are not parallel to y'y
		{
			x1 = width / 2;
			y1 = -(tlA * x1 + tlC) / tlB;

			x2 = x1 + 10;
			y2 = -(tlA * x2 + tlC) / tlB;
		}
		else
		// parallel to y'y
		{
			x1 = -(tlC / tlA);
			y1 = height / 2;

			x2 = x1;
			y2 = y1 + 10;
		}

		if (mB != 0.0)
		{
			x3 = (width / 2) - 1;
			y3 = -(mA * x3 + mC) / mB;
		}
		else
		{
			y3 = height / 2;
			x3 = -(mC / mA);
		}

		v1 = new Point2DDouble(x2 - x1, y2 - y1);
		v2 = new Point2DDouble(x3 - x1, y3 - y1);

		double V1norm = Math.sqrt(v1.x * v1.x + v1.y * v1.y);
		double V2norm = Math.sqrt(v2.x * v2.x + v2.y * v2.y);

		double sinA1 = (v1.y) / V1norm;
		double sinA2 = (v2.y) / V2norm;
		System.out.println("sinA1 = " + sinA1 + ", sinA2 = " + sinA2);
		double A = Math.asin(sinA2) - Math.asin(sinA1);
		double sinA = Math.sin(A);
		System.out.println("sinA is [" + sinA + "]");

		Vector result = new Vector();
		// gia kathe simio chekaroume an einai stin sosth plevra. sosth plevra =
		// antitheti apo aftin pou einai h medianLine.
		for (int i = 0; i < points.size(); ++i)
		{
			Point2DDouble p = (Point2DDouble) points.elementAt(i);
			v2 = new Point2DDouble(p.x - x1, p.y - y1);
			V2norm = Math.sqrt(v2.x * v2.x + v2.y * v2.y);

			double sinT = (v2.y) / V2norm;
			double F = Math.asin(sinT) - Math.asin(sinA1);
			double sinF = Math.sin(F);
			if ((sinA * sinF) < 0.0)
			{
				result.add(p);
			}
		}
		points.removeAll(result);
		return result;
	}

	/**
	 * getVanihingPoint 1. Get a set o lines. 2. Select 2 random lines. 3.
	 * Intersect them. 4. Find support from all other lines for a number of
	 * iterations. 5. return the most supported point with the support set.
	 * 
	 * @return
	 */

	public static boolean getVanishingPoint(int iter, Vector lineVec, Point2DDouble bestPoint, Vector supportVec, Vector ignoreVec)
	{
		Random rnd = new Random((new Date()).getTime());
		Line l1, l2;
		int count = iter;
		// if(count >= ((lineVec.size()*(lineVec.size()-1))/2))
		// count=lineVec.size();

		Point2DDouble p, bp = null;
		int bMsupp = 9999999, tempMsupp;
		Vector support, bsupport = new Vector();

		while ((count--) > 0)
		{
			int t1 = rnd.nextInt(lineVec.size());
			int t2 = rnd.nextInt(lineVec.size());
			if (t1 == t2)
			{
				continue;
			}

			l1 = (Line) lineVec.elementAt(t1);
			l2 = (Line) lineVec.elementAt(t2);

			p = intersectLines(l1, l2);

			if (p == null)
			{
				continue;
			}
			boolean ignore = false;
			for (int i = 0; i < ignoreVec.size(); ++i)
			{
				Point2DDouble pt = (Point2DDouble) ignoreVec.elementAt(i);
				if (p.distanceSq(pt) <= (16 * thresholdSq))
				{
					ignore = true;
					break;
				}
			}
			if (ignore)
				continue;

			support = distancesFromPoint(p, lineVec);
			if (support.size() > (bsupport.size()))
			{
				bsupport = support;
				bp = p;
			}
		}
		if (bp != null)
		{
			bestPoint.x = bp.x;
			bestPoint.y = bp.y;
			supportVec.addAll(bsupport);
			return true;
		}
		else
		{
			return false;

		}

	}

	public static Vector getLastResult()
	{
		return lastResult;
	}

	public static void setThreshold(int pixels)
	{
		threshold = pixels;
		thresholdSq = threshold * threshold;

	}

	public static int getThreshold()
	{
		return threshold;
	}
}
