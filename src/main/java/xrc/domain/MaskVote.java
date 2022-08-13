package xrc.domain;

import java.math.BigInteger;
import java.util.*;
public class MaskVote {

	//系统初始化
	//返回椭圆曲线群及椭圆曲线上一点h(h是公共参数)
	static List<Map> init(){
		Map<String, ECGroup> ecgroup_map = new HashMap<>();
		ECGroup ecgroup= new ECGroup();
		ecgroup_map.put("ECGroup", ecgroup);
		Map<String, ECPoint> ecpoint_map = new HashMap<>();
		ECPoint h = ecgroup.randompoint();
		ecpoint_map.put("h", h);
		List<Map> result = new ArrayList<Map>();
		result.add(ecgroup_map);
		result.add(ecpoint_map);
		return result;
	}


	//秘钥生成方法
	//输入为陷门值t和椭圆曲线群ecgroup
	//输出为2*(t+1)大小的数组fx,代表该用户生成的两个t次多项式的2*(t+1)个系数
	public static BigInteger[][] keygen(int t,ECGroup ecgroup){
		BigInteger fx[][] = new BigInteger[2][t+1];//选择t+1个a值和b值
		for(int i=0; i<=t; i++) {
			BigInteger a = new BigInteger(ecgroup.n.bitLength(), new Random());
			BigInteger b = new BigInteger(ecgroup.n.bitLength(), new Random());
			fx[0][i] = a;
			fx[1][i] = b;
			System.out.println(a);
			System.out.println(b);
		}

		return  fx;
	}


	//计算C
	//输入为陷门值t、用户的2*(t+1)个多项式系数fx、椭圆曲线群ecgroup、椭圆曲线点h
	//输出为该用户的t+1个C值
	static ECPoint[] C(int t,BigInteger[][] fx,ECGroup ecgroup,ECPoint h){
		ECPoint g = new ECPoint(ecgroup.x1,ecgroup.y1);
		ECPoint C[] = new ECPoint[t+1];
		for(int i=0; i<=t; i++)
			C[i] = ecgroup.add(ecgroup.multiply(g, fx[0][i]),ecgroup.multiply(h, fx[1][i]));
		return C;
	}


	//计算秘密值s
	//输入为发送方i，接收方j，陷门值t、发送方的2*(t+1)个多项式系数fx
	//输出为用户i要向用户j发送的两个秘密值sij及s’ij
	static BigInteger[] s(int i,int j,int t,BigInteger[][] fx){
		BigInteger s[] = new BigInteger[2];

		BigInteger ID_j = new BigInteger((j)+"");
		BigInteger sum1 = new BigInteger("0");
		BigInteger sum2 = new BigInteger("0");
		for(int k=0; k <= t; k++) {
			sum1 = sum1.add(fx[0][k].multiply(ID_j.pow(k)));
			sum2 = sum2.add(fx[1][k].multiply(ID_j.pow(k)));
		}
		s[0] = sum1;
		s[1] = sum2;
		return s;

	}


	//用户i验证用户j向自己发送的秘密值sji及s’ji是否正确
	//输入为验证方i、椭圆曲线群ecgroup、被验证方j向验证方i发送的秘密值s_ji(其中s_ji[0]=sji,s_ji[1]=s’ji)、椭圆曲线点h、门限值t、被验证方j的t+1个C值：C_j
	//输出为true/false
	static boolean verifi_s(int i, ECGroup ecgroup,BigInteger[] s_ji,ECPoint h,int t,ECPoint[] C_j) {
		ECPoint g = new ECPoint(ecgroup.x1,ecgroup.y1);
		boolean flag = true;
		ECPoint left = ecgroup.add(ecgroup.multiply(g, s_ji[0]),ecgroup.multiply(h, s_ji[1]));
		ECPoint right = new ECPoint();
		for(int k=0; k<=t; k++)
			right = ecgroup.add(right,ecgroup.multiply(C_j[k],new BigInteger((i)+"").pow(k)));
		if(left.x.compareTo(right.x) != 0 || left.y.compareTo(right.y) != 0)
			flag = false;

		return flag;
	}


	//计算A
	//输入为陷门值t、用户的2*(t+1)个多项式系数fx、椭圆曲线群ecgroup
	//输出为该用户的t+1个A值
	static ECPoint[] A(int t,BigInteger[][] fx,ECGroup ecgroup){
		ECPoint g = new ECPoint(ecgroup.x1,ecgroup.y1);
		ECPoint A[] = new ECPoint[t+1];
		for(int i=0; i<=t; i++)
			A[i] = ecgroup.multiply(g, fx[0][i]);
		return A;
	}


	//用户i验证用户j的t+1个A值A值是否正确
	//输入为验证方i、椭圆曲线群ecgroup、被验证方j向验证方i发送的秘密值sji、门限值t、被验证方j的t+1个A值：A_j
	//输出为true/false
	static boolean verifi_A(int i, ECGroup ecgroup,BigInteger s_ji0,int t,ECPoint[] A_j) {
		ECPoint g = new ECPoint(ecgroup.x1,ecgroup.y1);
		boolean flag = true;
		ECPoint left = ecgroup.multiply(g, s_ji0);
		ECPoint right = new ECPoint();
		for(int k=0; k<=t; k++)
			right = ecgroup.add(right,ecgroup.multiply(A_j[k],new BigInteger((i)+"").pow(k)));
		if(left.x.compareTo(right.x) != 0 || left.y.compareTo(right.y) != 0)
			flag = false;
		return flag;
	}


	//计算联合公钥Y
	//输入为每个参与投票的用户的A[0]值组成的数组A0_list
	//输出为联合公钥Y
	static ECPoint Y(ECPoint[] A0_list,ECGroup ecgroup) {
		ECPoint Y = new ECPoint();
		int len = A0_list.length;
		for(int i=0; i<len; i++)
			Y = ecgroup.add(Y,A0_list[i]);
		return Y;
	}


	//加密用户投票
	//输入为用户投票vote，椭圆曲线群ecgroup，椭圆曲线点h，联合公钥Y
	//输出为加密后的投票
	static ECPoint[] encrypt(int vote, ECGroup ecgroup,ECPoint h,ECPoint Y) {
		ECPoint g = new ECPoint(ecgroup.x1,ecgroup.y1);
		ECPoint en_vote[] = new ECPoint[2];
		BigInteger r = new BigInteger(ecgroup.n.bitLength(), new Random());
		en_vote[0] = ecgroup.add(ecgroup.multiply(h, new BigInteger(vote+"")),ecgroup.multiply(Y, r));
		en_vote[1] = ecgroup.multiply(g, r);
		return en_vote;
	}


	//对所有加密的投票相乘
	//输入为所有加密的投票组成的数组en_vote_list，椭圆曲线群ecgroup
	//输出为所有加密的投票相乘之后的结果
	static ECPoint[] mul_en_vote(ECPoint[][] en_vote_list,ECGroup ecgroup) {
		ECPoint E[] = new ECPoint[2];
		E[0] = new ECPoint();
		E[1] = new ECPoint();
		int len = en_vote_list.length;
		for(int i=0; i<len; i++) {
			E[0] = ecgroup.add(E[0],en_vote_list[i][0]);
			E[1] = ecgroup.add(E[1],en_vote_list[i][1]);
		}
		return E;
	}


	//用户使用自己的私钥对g^R进行加密(g^R为mul_en_vote()求出的E[1])
	//输入为椭圆曲线群ecgroup,g^R:g_R,用户私钥x
	//输出为加密后的g^R
	static ECPoint en_g_R(ECGroup ecgroup,ECPoint g_R,BigInteger x) {
		return ecgroup.multiply(g_R,x);
	}


	//计算总票数sum
	//输入为椭圆曲线群ecgroup，所有加密后的g^R组成的数组en_g_R_list，椭圆曲线点h，mul_en_vote()求出的E[0]：E0
	//输出为总票数sum
	static int sum(ECGroup ecgroup,ECPoint[] en_g_R_list,ECPoint h,ECPoint E0) {
		//求Y^R
		ECPoint Y_R = new ECPoint();
		int len = en_g_R_list.length;
		for(int i=0; i<len; i++) {
			Y_R = ecgroup.add(Y_R,en_g_R_list[i]);
		}

		//求h^sum
		ECPoint p = new ECPoint(Y_R.x,ecgroup.p.subtract(Y_R.y));
		ECPoint h_sum = ecgroup.add(E0, p);

		//求sum
		int sum = -1;
		if (h_sum.isO()) {
			sum = 0;
		}else {
			boolean flag = false;
			for(int i=1; i<=len; i++) {
				ECPoint h_i = ecgroup.multiply(h, new BigInteger(i+""));
				if( h_i.x.compareTo(h_sum.x)==0 && h_i.y.compareTo(h_sum.y)==0 ) {
					flag = true;
					sum = i;
					break;
				}
			}
		}
		return sum;
	}

	public static void main(String arg[]) {
		/*************************************************************系统初始化*************************************************************/
		int n=10;
		int t=7;
		ECGroup ecgroup = new ECGroup();
		ECPoint g = new ECPoint(ecgroup.x1,ecgroup.y1);
		ECPoint h = ecgroup.randompoint();

		/*************************************************************私钥生成阶段*************************************************************/
		//每个用户选择两个t次多项式
		BigInteger fx[][][] = new BigInteger[n][2][t+1];
		for(int i=0; i<n; i++)
			fx[i] = keygen(t,ecgroup);

		//每个用户公布Cik
		ECPoint C[][] = new ECPoint[n][t+1];
		for(int i=0; i<n; i++)
			C[i] = C(t, fx[i],ecgroup, h);

		//计算每个用户要向其他用户发送的秘密值
		BigInteger s[][][] = new BigInteger[n][n][2];
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++)
				s[i][j]=s(i,j,t, fx[i]);
		}

		//验证s及s'是否正确
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				boolean flag = verifi_s(i, ecgroup, s[j][i], h,t, C[j]);
				if(flag == false)
					System.out.println("验证失败，i="+i+"j="+j);
			}
		}

		/*************************************************************生成联合公钥*************************************************************/
		//每个个用户i广播Aik,k=0,...,t
		ECPoint A[][] = new ECPoint[n][t+1];
		for(int i=0; i<n; i++)
			A[i]= A(t,fx[i],ecgroup);

		//验证A是否正确
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				boolean flag = verifi_A(i, ecgroup, s[j][i][0], t, A[j]);
				if(flag == false)
					System.out.println("验证失败，i="+i+"j="+j);
			}
		}

		//计算联合公钥Y
		ECPoint Y = new ECPoint();
		ECPoint[] A0_list = new ECPoint[n];
		for(int i=0; i<n; i++) {
			A0_list[i] = A[i][0];
		}
		Y=Y(A0_list,ecgroup);

		/*************************************************************对投票进行加密*************************************************************/
		int vote[] = new int[] {0,0,1,0,0,0,1,0,0,0};//每个用户的投票，0代表不同，1代表同意

		ECPoint en_vote[][] = new ECPoint[n][2];//每个用户加密后的投票
		for(int i=0; i<n; i++)
			en_vote[i] = encrypt(vote[i], ecgroup, h, Y);

		/*************************************************************联合解密*************************************************************/
		//对所有加密的投票相乘
		ECPoint E[] = new ECPoint[2];//对每个加密后的投票相乘的结果
		E = mul_en_vote(en_vote,ecgroup);

		//每个用户用自己的私钥对g^R进行加密
		ECPoint en_g_R[] = new ECPoint[n];
		for(int i=0; i<n; i++)
			en_g_R[i] = en_g_R(ecgroup, E[1], fx[i][0][0]);

		//求sum
		int sum = -1;
		sum = sum(ecgroup,en_g_R, h,E[0]);
		System.out.println(sum);
	}


}

