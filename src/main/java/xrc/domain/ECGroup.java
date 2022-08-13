package xrc.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class ECGroup {


	//以下是椭圆曲线群的参数
	//椭圆曲线方程为：y^2=x^3+ax+b mod p(a,b∈GF(p)),(x1,y1)为椭圆曲线群的生成元，阶为n.
	BigInteger p;
	BigInteger a;
	BigInteger b;
	BigInteger n;
	BigInteger x1;
	BigInteger y1;



	public ECGroup() {
		this.p = (new BigInteger("2").pow(224)).subtract(new BigInteger("2").pow(96)).subtract(new BigInteger("1"));
		this.a = new BigInteger("-3");
		this.b = new BigInteger("B4050A850C04B3ABF54132565044B0B7D7BFD8BA270B39432355FFB4",16);
		this.n = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFF16A2E0B8F03E13DD29455C5C2A3D",16);
		this.x1 = new BigInteger("B70E0CBD6BB4BF7F321390B94A03C1D356C21122343280D6115C1D21",16);
		this.y1 = new BigInteger("BD376388B5F723FB4C22DFE6CD4375A05A07476444D5819985007E34",16);
	}

	public ECGroup(BigInteger p, BigInteger a, BigInteger b, BigInteger n, BigInteger x1, BigInteger y1) {
		this.p = p;
		this.a = a;
		this.b = b;
		this.n = n;
		this.x1 = x1;
		this.y1 = y1;
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

	/*
	BigInteger sqrt(BigInteger num) {
		if(num.compareTo(BigInteger.ZERO)<0)
			return null;
		String num_string = String.valueOf(num);
		String sqrt="0"; //开方结果
		String pre="0"; //开方过程中需要计算的被减数
		BigInteger trynum; //试商，开放过程中需要计算的减数
		BigInteger flag;  //试商，得到满足要求减数的之后一个数
		BigInteger twenty=new BigInteger("20"); //就是20
		BigInteger dividend; ///开方过程中需要计算的被减数
		int len=num_string.length(); //数字的长度

		if(len%2==0){ //长度为偶数
			for(int i=0;i<len/2;++i){ //得到的平方根一定是len/2位
				dividend=new BigInteger(pre+num_string.substring(2*i,2*i+2));
				for(int j=0;j<=9;++j){
					trynum=twenty.multiply(new BigInteger(sqrt)).multiply(new BigInteger(j+"")).add(new BigInteger(j+"").multiply(new BigInteger(j+"")));
					flag=twenty.multiply(new BigInteger(sqrt)).multiply(new BigInteger((j+1)+"")).add(new BigInteger((j+1)+"").multiply(new BigInteger((j+1)+"")));;

					//满足要求的j使得试商与计算中的被减数之差为最小正数
					if(trynum.subtract(dividend).compareTo(BigInteger.ZERO)<=0 && flag.subtract(dividend).compareTo(BigInteger.ZERO)>0){
						sqrt+=j;  //结果加上得到的j
						pre=dividend.subtract(trynum).toString(); //更新开方过程中需要计算的被减数
						break;
					}
				}
			}
	　　}
	}

	public static boolean is_square(BigInteger num){
        // 牛顿法求解平方根, 求解a的平方根
        // x为a的平方根，x的初始值为1， 按x = (x+a/x)/2迭代， 误差为error
        BigDecimal x = BigDecimal.ONE;
        BigDecimal a = new BigDecimal(num.toString());
        BigDecimal eps = new BigDecimal("1");
        final BigDecimal error = new BigDecimal("1E-10");
        int scale = 100;
        // 进入循环
        while(eps.compareTo(error) == 1){ // eps > error
            x = x.add(a.divide(x, scale, BigDecimal.ROUND_HALF_UP)).divide(new BigDecimal("2.0"), scale, BigDecimal.ROUND_HALF_UP);
            eps = x.multiply(x).subtract(a).abs();
        }
        BigInteger sqrt = x.toBigInteger();  // 求平方根的整数部分
        if(sqrt.pow(2).compareTo(num) == 0)
            return true;
        else
            return false;
    }*/

	public ECPoint randompoint() {//从椭圆曲线中任意选取一个点
		BigInteger x = new BigInteger(n.bitLength(), new Random());
		ECPoint ep = new ECPoint(x1,y1);
		ECPoint rp = multiply(ep,x);

		return rp;


	}


	BigInteger o(ECPoint p) {//求椭圆曲线中某一点的阶
		BigInteger r = new BigInteger("1");
		while (! p.isO()) {
			r = r.add(new BigInteger("1"));
			p = multiply(p, r);
		}
		return r;
	}

	 ECPoint multiply(ECPoint p, BigInteger n) {//椭圆曲线点乘运算
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

	ECPoint add(ECPoint p1, ECPoint p2) {//椭圆曲线中两个点的加法运算
		if (p1.isO()) {
			ECPoint result = new ECPoint();
			result.x = p2.x;
			result.y = p2.y;
			return result;
		}
		if (p2.isO()) {
			ECPoint result = new ECPoint();
			result.x = p1.x;
			result.y = p1.y;
			return result;
		}
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

	public static void main(String[ ] args) {

		ECGroup ecgroup = new ECGroup();
		ECPoint g = new ECPoint(ecgroup.x1,ecgroup.y1);
		BigInteger a = new BigInteger(ecgroup.n.bitLength(), new Random());
		BigInteger z = new BigInteger("5");
		int t = 3;
		BigInteger azt=a.multiply(z.pow(t));
		ECPoint p1 = ecgroup.multiply(g, azt);
		ECPoint p2 = ecgroup.multiply(g, a);
		ECPoint p3 = ecgroup.multiply(p2, z.pow(t));
		System.out.println("("+p1.x+","+p1.y+")");
		System.out.println("("+p3.x+","+p3.y+")");
	}
}

