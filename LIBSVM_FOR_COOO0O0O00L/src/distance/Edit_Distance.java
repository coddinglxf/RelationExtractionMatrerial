package distance;

public class Edit_Distance {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double f1[]={1,2,3,3,2,4,5};
		double f2[]={6,2,3,3,7,4};
		Edit_Distance distance=new Edit_Distance();
		distance.edit_distance(f1, f2);
	}
	public int edit_distance(double f1[],double f2[])
	{
		int distance[][]=new int[f1.length+1][f2.length+1];
		distance[0][0]=0;
		for(int i=1;i<distance.length;i++)
		{
			distance[i][0]=i;
		}
		for(int i=1;i<distance[0].length;i++)
		{
			distance[0][i]=i;
		}
		
		for(int i=0;i<f1.length;i++)
		{
			for(int j=0;j<f2.length;j++)
			{
				int x=i+1;
				int y=j+1;
				if(f1[i]==f2[j])
				{
					distance[x][y]=Math.min(distance[x][y-1]+1, Math.min(distance[x-1][y]+1, distance[x-1][y-1]));
				}
				else
				{
					distance[x][y]=Math.min(distance[x][y-1]+1, Math.min(distance[x-1][y]+1, distance[x-1][y-1]+1));
				}
			}
		}
		return distance[f1.length][f2.length];
	}
}
