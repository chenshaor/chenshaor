package xrc.domain;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class ECPoint {
	BigInteger x;//椭圆曲线点的x坐标
	BigInteger y;//椭圆曲线点的y坐标


	public ECPoint() {
		x = null;
		y = null;
	}

	public ECPoint(BigInteger x, BigInteger y) {
		this.x = x;
		this.y = y;
	}


	public String toString() {
		if (isO())
			return "O";
		return "(" + x.toString(16) + ", " + y.toString(16) + ")";
	}

	boolean isO() {
		if (x == null && y == null)
			return true;
		return false;
	}


/*
	static ECPoint add(ECPoint p1, ECPoint p2) {//椭圆曲线中两个点的加法运算
		if (p1.isO()) return p2;
		if (p2.isO()) return p1;
		ECPoint p3 = new ECPoint();
		BigInteger lambda;
		if (p1.x.compareTo(p2.x) == 0) {
			if (p1.y.compareTo(p2.y) == 0) {
				lambda = new BigInteger("3").multiply(p1.x.pow(2)).add(a).multiply(new BigInteger("2").multiply(p1.y).modPow(new BigInteger("-1"), p)).mod(p);
				p3.x = lambda.pow(2).subtract(new BigInteger("2").multiply(p1.x)).mod(p);
				p3.y = lambda.multiply(p1.x.subtract(p3.x)).subtract(p1.y).mod(p);
				return p3;
			}
			if (p1.y.compareTo(p.subtract(p2.y)) == 0)
				return p3;
		}
		lambda = p2.y.subtract(p1.y).multiply(p2.x.subtract(p1.x).modPow(new BigInteger("-1"), p)).mod(p);
		p3.x = lambda.pow(2).subtract(p1.x).subtract(p2.x).mod(p);
		p3.y = lambda.multiply(p1.x.subtract(p3.x)).subtract(p1.y).mod(p);
		return p3;
	}

	static ECPoint multiply(ECPoint p, BigInteger n) {//椭圆曲线点乘运算
		ECPoint q = add(p, new ECPoint());
		ECPoint r = new ECPoint();
		do {
			if (n.and(new BigInteger("1")).intValue() == 1)
				r = add(r, q);
			q = add(q, q);
			n = n.shiftRight(1);
		} while (n.intValue() != 0);
		return r;
	}

	List<ECPoint> solutionPoints() {//求椭圆曲线群中的所有点
		List<ECPoint> r = new ArrayList<ECPoint>();
		List<BigInteger> l = new ArrayList<BigInteger>();
		for (BigInteger y = new BigInteger("1"); y.compareTo(p.divide(new BigInteger("2"))) != 1; y = y.add(new BigInteger("1")))
			l.add(y.modPow(new BigInteger("2"), p));
		for (BigInteger x = new BigInteger("0"); x.compareTo(p) == -1; x = x.add(new BigInteger("1"))) {
			BigInteger t = x.pow(3).add(a.multiply(x)).add(b).mod(p);
			if (isExist(t, l) != -1) {
				BigInteger y = new BigInteger(isExist(t, l) + "");
				r.add(new ECPoint(x, y));
				r.add(new ECPoint(x, p.subtract(y)));
			}
		}
		r.add(new ECPoint());
		return r;
	}

	static int isExist(BigInteger b, List<BigInteger> l) {
		for (int i = 0; i < l.size(); i++)
			if (l.get(i).compareTo(b) == 0) return (i + 1);
		return -1;
	}

	BigInteger o(ECPoint p) {//求椭圆曲线中某一点的阶
		BigInteger r = new BigInteger("1");
		while (! p.isO()) {
			r = r.add(new BigInteger("1"));
			p = multiply(p, r);
		}
		return r;
	}

	static BigInteger[] keygen(){
		ECPoint G = new ECPoint(x1,y1);
		BigInteger d = new BigInteger(n.bitLength(), new Random());
		ECPoint Q = multiply(G, d);


		BigInteger[] Key = new BigInteger[3];
		Key[0] = d;
		Key[1] = Q.x;
		Key[2] = Q.y;
		return Key;
	}


	static BigInteger[] encrypt(BigInteger M, ECPoint Q) {
		ECPoint G = new ECPoint(x1,y1);

		BigInteger k;
		ECPoint X1, X2;
		do {
			k = new BigInteger(n.bitLength(), new Random());
		} while ((X2 = multiply(Q, k)).x == null);
		X1 = multiply(G, k);


		BigInteger[] C = new BigInteger[3];
		C[0] = X1.x;
		C[1] = X1.y;
		C[2] = M.multiply(X2.x).mod(n);
		return C;
	}

	static BigInteger decrypt(BigInteger[] C, BigInteger d) {
		ECPoint X1 = new ECPoint(C[0], C[1]);
		ECPoint X2 = multiply(X1, d);
		BigInteger M = C[2].multiply(X2.x.modPow(new BigInteger("-1"), n)).mod(n);
		return M;
	}

	public static void main(String[ ] args) {
		BigInteger M = new BigInteger("1234567890abcdef");
		BigInteger[] Key = ECPoint.keygen();
		ECPoint Q = new ECPoint(Key[1], Key[2]);
		BigInteger[] Cipher = ECPoint.encrypt(M,Q);
		BigInteger De_M = ECPoint.decrypt(Cipher,Key[0]);
		System.out.println(M);
		System.out.println(Cipher[0]);
		System.out.println(Cipher[1]);
		System.out.println(Cipher[2]);
		System.out.println(De_M);
	}
	*/

}

