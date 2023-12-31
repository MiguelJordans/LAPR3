#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <errno.h>
#include "energy_needed.h"
#include "calculate_energy_consumption.h"
#include "fill_matrix.h"

//Container size (20 Bytes)

//Global variable needed to pass the number of containers to other functions
int numContainers = 0;

int main(void) {	
	//Size of the Container Structure	
	const char CONTAINER_SIZE = sizeof(Container);
	
	//Allocate the memory to create an "3D" Matrix for containers
	Container* container_array = malloc(CONTAINER_SIZE * ARRAY_SIZE);
	
	//Pointer to the file with the containers information
	FILE* fp = fopen(FILE_NAME, "r");
	
	//If, for some reason, the file doesn't exist, we should stop the program here
	if(fp == NULL) {
		perror("Unable to open file!");
		return EXIT_FAILURE;
	}
	
	//Fill dynamically the array with the containers information
	container_array = fill_matrix(container_array,fp);
	
	//Verify the integrity of the array
	if(container_array == NULL) {
		perror("Unable to read the file! There is missinformation in it.");
		return EXIT_FAILURE;
	}
	
	//Control variables
	char exit = 0;
	char option;
	char x,y,z;
	char count = 0;
	float genOutput;
	int decision;
	
	//Prepare the Menu
	print_menu();

	do {
		printf("Please type the option desired!\n");
		scanf("%hhd",&option);
		
		switch(option){
			case(1):
			//Extra, in case it is necessary to view the container array (only the occupied positions)
			print_container_array(container_array);
			count++;
			break;
			
			case(2):
			//US 410, know the amount needed energy, first, it is necessary to have the x,y,z coord in order to know which container we will be working with
			printf("\nPlease enter the x position: \n");
			scanf("%hhd",&x);

			printf("Please enter the y position: \n");
			scanf("%hhd",&y);

			printf("Please enter the z position: \n");
			scanf("%hhd",&z);
	
			char temp_position = find_container_position(container_array, x, y, z);

			if(temp_position == -1) {
				printf("\nPlease enter a valid position! Check the inserted positions and try again.\n\n");
				count++;
				break;

			} else {
				float energyNeeded = energy_needed(container_array, x, y, z);
				
				if(energyNeeded == -1){
					printf("\nThe Container is not refrigerated!\n");
				} else {
					printf("\nEnergy needed to keep the container at its required temperature: %.2f J\n\n", energyNeeded);
					count++;
					break;
				}
			}
			
			case(3):
			//US 411, can the ship's energy generation output support the container's energy consumption in a determined cargo manifest
			printf("\nEnter the ship's energy generation output: \n");
			scanf("%f",&genOutput);

			decision = calculate_energy_consumption(container_array, genOutput, numContainers);
			
			if(decision == 2) {
				printf("\nFor the given generator capacity, the given energy generation is not enough to provide energy to all refrigerated containers!\n\n");
			} else if (decision == 1) {
				printf("\nFor the given generator capacity, the given energy generation is enough to provide energy to all refrigerated containers!\n\n");
			}
			count++;
			break;
			
			case(4):
			//Option to exit the program
			exit++;
			break;
			
			default:
			printf("Please enter a valid option!\n");
			break;
			
		}
		
		if(count == 2) {
			print_menu();
			count = 0;
		}
		
	} while(exit == 0);
	
	free(container_array);
	
	return EXIT_SUCCESS;
}
