#include <stdio.h>

int BIT[1000], arr[1000];
int size;

int query(int index){
	int sum = 0;
	for(;index > 0; index-=(index&-index))
		sum+=BIT[index];
	return sum;
}

void update(int index, int delta){
	for(; index<=size; index+=(index&-index))
		BIT[index] += delta;
}

void update_array(int index, int new_value){
	int delta = new_value - arr[index];
	arr[index] = new_value;
	update(index, delta);
}

int main(){
     scanf("%d", &size);
     int i;
     for(i = 1; i <= size; i++)
     {
           scanf("%d", &arr[i]);
           update(i, arr[i]);
     }
     printf("sum of first 10 elements is %d\n", query(10));
     printf("sum of all elements in range [2, 7] is %d\n", query(7) - query(2-1));
     return 0;
}