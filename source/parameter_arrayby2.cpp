#include <stdio.h>

void tr2(int arr[3][3])
{
	printf("tr2 - %d\n",arr[0][0]);
}

int *tr1()
{
	int a[3][3]={{1,2,3},{4,5,6},{7,8,9}};
	
	return a;
}

int main()
{
	int* c = tr1();
	printf("main - %d\n",c[1][1]);
	
	int b[3][3] = {{4,5,6},{4,5,6},{4,5,6}};
	tr2(b);
	
	return 0;
}

