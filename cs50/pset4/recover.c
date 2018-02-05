#include <stdio.h>

int isJpegStart(unsigned char bytes[]);
void writeJPEGToFile(int fileNum, FILE *cardptr);
char *getFilenameFormat(int fileNum);

const int BLOCK_SIZE = 512;

int main(int argc, char *argv[])
{
    // ensure proper usage
    if (argc != 2)
    {
        fprintf(stderr, "Usage: recover file \n");
        return 1;
    }

    char *filename = argv[1];
    printf("%s\n", filename);

    FILE *cardptr = fopen(filename, "r");
    if (cardptr == NULL)
    {
        fprintf(stderr, "File could not be opened\n");
        return 2;
    }

    int fileNum = 0;
    //unsigned char is 1 byte but normal char is not
    unsigned char bytes[4];

    //Read bytes 1,2,3,4 at a time
    //Check if its start of jpeg
    //otherwise go back to byte 2 and repeat the process
    while (fread(&bytes, sizeof(bytes), 1, cardptr) == 1)
    {
        if (isJpegStart(bytes) == 1)
        {
            //go to byte from where jpeg starts
            fseek(cardptr, -4, SEEK_CUR);
            writeJPEGToFile(fileNum++, cardptr);
        }
        else
        {
            //go 3 bytes back and read 4 bytes from there
            fseek(cardptr, -3, SEEK_CUR);
        }
    }

    fclose(cardptr);
}

//Start of JPEG is 0xff=255 0xd8=216 0xff=216 0xe0=224 to 0xef=239
int isJpegStart(unsigned char bytes[])
{
    if (((int)bytes[0]) == 255
        && ((int)bytes[1]) == 216
        && ((int)bytes[2]) == 255
        && (((int)bytes[3]) >= 224 && ((int)bytes[3]) <= 239))
    {
        //printf("JPEG FOUND \n");
        return 1;
    }

    return 0;
}


//Write jpeg file
void writeJPEGToFile(int fileNum, FILE *cardptr)
{
    unsigned char bytes[BLOCK_SIZE];

    //Create dynamic file name by appending file number
    char filename[17];
    sprintf(filename, getFilenameFormat(fileNum), fileNum);

    FILE *outptr = fopen(filename, "w");
    int firstByte = 1;

    while (fread(&bytes, BLOCK_SIZE, 1, cardptr) == 1)
    {
        //firstByte flag check to make sure jpeg check starts from 2nd block onwards
        if (firstByte++ > 1 && isJpegStart(bytes) == 1)
        {
            //If its start of JPEG go to start of block before returning from function
            fseek(cardptr, -1 * BLOCK_SIZE, SEEK_CUR);
            break;
        }

        fwrite(&bytes, BLOCK_SIZE, 1, outptr);
    }

    fclose(outptr);
}

//Format filename to always 3 decimal
char *getFilenameFormat(int fileNum)
{
    if (fileNum < 10)
    {
        return "00%i.jpg";
    }
    else if (fileNum < 99)
    {
        return "0%i.jpg";
    }
    else
    {
        return "%i.jpg";
    }
}
