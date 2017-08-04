extern "C"

// int DAT_Size           // int MAX index of DAT
// int FAMILY_Size     // int	SIZE Family
// long long *DAT        // long[numberDAT][indexInDAT]
// int DAT_Lenght       // int
// int level_1               // int   size first level
// int level_2
// int level_3
// int level_4
// long long *link1_2    // long[indexFamily][index]
// long long *link2_3    // long[indexFamily][index]
// long long *link3_4    // long[indexFamily][index]

__global__ void neural(
				int DAT_Size,
				int DAT_Lenght,
				int FAMILY_Size,
				long long *DAT,
				int level_1,
				int level_2,
				int level_3,
				int level_4,
				long long *link1_2,
				long long *link2_3,
				long long *link3_4,
				long long *result)
{	
	int j = blockIdx.x * blockDim.x * blockDim.y + threadIdx.y * blockDim.x + threadIdx.x;
	//int idx = threadIdx.x + (((gridDim.x * blockIdx.y) + blockIdx.x)*blockDim.x);
	//int j = blockIdx.x * blockDim.x + threadIdx.x; //thread index	
	if (j<FAMILY_Size * DAT_Size){
	
		int NumberFamily = (int)(j - (FAMILY_Size * ((int)j/FAMILY_Size)));
		int NumberDAT = (int)(j/FAMILY_Size);
		
		long long datlev2[@second_lev@]; //level_2
		long long datlev3[@tride_lev@]; //level_3
		long long datlev4[1]; //level_4
		long long startbit = 1;
		
			// **************  LEVEL 1 - 2   ************************ //			
			int m = 0; // num link 1 - 2
			int currlev[64];
			long long addData;
			long long middleval = 0;
			
			for (int k = 0; k < level_2; ++k) {							
				for (int i = 0; i < DAT_Lenght; ++i){
						int DAT_index = i+NumberDAT*DAT_Lenght;// array2Index(i, NumberDAT, DAT_Lenght); //return x + y*height;
						int link1_2_index = m+NumberFamily*level_1*level_2; // array2Index(m, NumberFamily, level_1); //return x + y*height;
						if ( DAT[DAT_index] != 0) {
							addData = DAT[DAT_index]&link1_2[link1_2_index];
							for (int f = 0; f < 64; ++f){					
								currlev[f] = currlev[f] + (int)((addData>>f)&1);
							}						
							++m;
						}
					}
					int lev = 0;
					for (int f = 0; f < 64; ++f){
						if (currlev[f] > lev){
							lev = currlev[f];
							}
					}
					lev = lev >> 1;
					for (int f = 0; f < 64; ++f){
						if (currlev[f] >= lev){datlev2[k] = datlev2[k]^(startbit<<f);}
					}
					//if(middleval == 0){ middleval = datlev2[k];}
					for (int f = 0; f < 64; ++f){
						currlev[f] = 0;
					}
			}			
			// **************  END LEVEL 1 - 2   ************************ //
			// **************  LEVEL 2 - 3   ************************ //
			m = 0; // num link 2 - 3
			for (int k = 0; k < level_3; ++k) {							
				for (int i = 0; i < level_2; ++i){
						int link2_3_index = m+NumberFamily*level_2*level_3; // array2Index(m, NumberFamily, level_1); //return x + y*height;
						addData = datlev2[i]&link2_3[link2_3_index];
						for (int f = 0; f < 64; ++f){	
							currlev[f] = currlev[f] + (int)((addData>>f)&1);
						}						
						++m;
					}
					
					int lev = 0;
					for (int f = 0; f < 64; ++f){
						if (currlev[f] > lev){
							lev = currlev[f];
							}
					}
					lev = lev >> 1;
					for (int f = 0; f < 64; ++f){
						if (currlev[f] >= lev){datlev3[k] = datlev3[k]^(startbit<<f);}
					}
					for (int f = 0; f < 64; ++f){
						currlev[f] = 0;
					}
			}
			// **************  END LEVEL 2 - 3   ************************ //
			// **************  LEVEL 3 - 4   ************************ //
			m = 0; // num link 3 - 4
			for (int k = 0; k < level_4; ++k) {							
				for (int i = 0; i < level_3; ++i){
						int link3_4_index = m+NumberFamily*level_3*level_4; // array2Index(m, NumberFamily, level_1); //return x + y*height;
						addData = datlev3[i]&link3_4[link3_4_index];
						for (int f = 0; f < 64; ++f){	
							currlev[f] = currlev[f] + (int)((addData>>f)&1);
						}						
						++m;
					}
					
					int lev = 0;
					for (int f = 0; f < 64; ++f){
						if (currlev[f] > lev){
							lev = currlev[f];
							}
					}
					lev = lev >> 1;
					for (int f = 0; f < 64; ++f){
						if (currlev[f] >= lev){datlev4[k] = datlev4[k]^(startbit<<f);}
					}
			}
			// **************  END LEVEL 3 - 4   ************************ //
			
			result[j] = datlev4[0]; 
	}
}

