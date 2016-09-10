
package VPC;

public class MatrixAlgebra
{

	public static Matrix product(Matrix A, Matrix B) throws Exception
	{
		return Matrix.times(A, B);
	}

	public static double[][] product(double[][] A, double[][] B) throws Exception
	{
		Matrix a = new Matrix(A);
		Matrix b = new Matrix(B);
		Matrix res = Matrix.times(a, b);

		return res.getArrayPointer();
	}

	public static double[] product(double[][] H, double[] V)
	{
		if (V.length != H[0].length)
		{
			System.out.println("Error : Cannot multiply these matrices :" + V + "\nand\n" + H);
		}

		double[][] Vd = new double[1][3];
		Vd[0][0] = V[0];
		Vd[0][1] = V[1];
		Vd[0][2] = V[2];

		Matrix v = new Matrix(Vd);

		Matrix h = new Matrix(H);

		Matrix res = v.times(h);
		double[][] resA = res.getArrayPointer();

		double[] result = new double[H.length]; // nxk
		result[0] = resA[0][0];
		result[1] = resA[0][1];
		result[2] = resA[0][2];

		return (result);
	}

	/**
	 * @param A
	 * @return A^(-1)
	 */
	public static double[][] inverse(double[][] A)
	{
		Matrix m = new Matrix(A);
		Matrix res = m.inverse();
		return res.getArrayPointer();
	}

	public static Matrix product(double cons, Matrix A)
	{
		return A.times(cons);
	}

	public static Matrix matrixT(Matrix A)
	{
		return A.transpose();
	}

	public static double dotProduct(double[] x, double[] y)
	{
		double result = 0.0;

		for (int i = 0; i < x.length; ++i)
		{
			result += x[i] * y[i];
		}
		return result;
	}

	public static double[] crossProduct(double[] x, double[] y)
	{
		double[] result = new double[3];

		result[0] = x[1] * y[2] - x[2] * y[1];
		result[1] = x[0] * y[2] - x[2] * y[0];
		result[2] = x[0] * y[1] - x[1] * y[0];

		return result;
	}

	public static double determinant(Matrix M)
	{
		return M.determinant();
	}

	public static double determinant(double[][] m)
	{
		return (new Matrix(m)).determinant();
	}
}
