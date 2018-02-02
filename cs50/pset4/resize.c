// Copies a BMP file

#include <stdio.h>
#include <stdlib.h>

#include "bmp.h"

/*
    Info Header
        biWidth - width in pixels (without padding)
        biHeight - height in pixels
        biSizeImage - image size in bytes (including padding)
        biSizeImage = ((sizeof(RGBTRIPLE) * biWidth + padding) * abs(biHeight);
        padding = (4 - (biwidth * sizeof(RGBTRIPLE)) % 4) % 4;

    File Header
        bfSize - total size of file in bytes = headers + pixels + padding;
        bfSize = biSizeImage + sizeof(BITMAPINFOHEADER) + sizeof(BITMAPFILEHEADER);

    Solution Hints
        biWidth = biWidth * n
        biHeight = biHeight * n
        padding = (4 - (biwidth * sizeof(RGBTRIPLE)) % 4) % 4;
        biSizeImage = ((sizeof(RGBTRIPLE) * biWidth + padding) * abs(biHeight);
        bfSize = biSizeImage + sizeof(BITMAPINFOHEADER) + sizeof(BITMAPFILEHEADER);
        padding = ?

    Writing padding
        We use fputc(char, outptr) i.e. fputc(0x00, outfile);
*/

int main(int argc, char *argv[])
{
    // ensure proper usage
    if (argc != 4)
    {
        fprintf(stderr, "Usage: resize n infile outfile\n");
        return 1;
    }

    // remember filenames
    int n = atoi(argv[1]);
    if (n <= 0 || n > 100) {
        fprintf(stderr, "n must be between 1 and 100\n");
        return 1;
    }

    char *infile = argv[2];
    char *outfile = argv[3];

    // open input file
    FILE *inptr = fopen(infile, "r");
    if (inptr == NULL)
    {
        fprintf(stderr, "Could not open %s.\n", infile);
        return 2;
    }

    // open output file
    FILE *outptr = fopen(outfile, "w");
    if (outptr == NULL)
    {
        fclose(inptr);
        fprintf(stderr, "Could not create %s.\n", outfile);
        return 3;
    }

    // read infile's BITMAPFILEHEADER
    BITMAPFILEHEADER bf;
    fread(&bf, sizeof(BITMAPFILEHEADER), 1, inptr);

    // read infile's BITMAPINFOHEADER
    BITMAPINFOHEADER bi;
    fread(&bi, sizeof(BITMAPINFOHEADER), 1, inptr);

    // ensure infile is (likely) a 24-bit uncompressed BMP 4.0
    if (bf.bfType != 0x4d42 || bf.bfOffBits != 54 || bi.biSize != 40 ||
        bi.biBitCount != 24 || bi.biCompression != 0)
    {
        fclose(outptr);
        fclose(inptr);
        fprintf(stderr, "Unsupported file format.\n");
        return 4;
    }

    //Declare new variables for outfile headers
    BITMAPINFOHEADER biOutfile;
    biOutfile.biSize = bi.biSize;
    biOutfile.biWidth = bi.biWidth;
    biOutfile.biHeight = bi.biHeight;
    biOutfile.biPlanes = bi.biPlanes;;
    biOutfile.biBitCount = bi.biBitCount;
    biOutfile.biCompression = bi.biCompression;
    biOutfile.biSizeImage = bi.biSizeImage;
    biOutfile.biXPelsPerMeter = bi.biXPelsPerMeter;
    biOutfile.biYPelsPerMeter = bi.biYPelsPerMeter;
    biOutfile.biClrUsed = bi.biClrUsed;
    biOutfile.biClrImportant = bi.biClrImportant;

    BITMAPFILEHEADER bfOutfile;
    bfOutfile.bfType = bf.bfType;
    bfOutfile.bfSize = bf.bfType;
    bfOutfile.bfReserved1 = bf.bfReserved1;
    bfOutfile.bfReserved2 = bf.bfReserved2;
    bfOutfile.bfOffBits = bf.bfOffBits;

    //determine height width
    biOutfile.biWidth = bi.biWidth * n;
    biOutfile.biHeight = bi.biHeight * n;

    // determine padding for scanlines
    int oldPadding = (4 - (bi.biWidth * sizeof(RGBTRIPLE)) % 4) % 4;
    int newPadding = (4 - (biOutfile.biWidth * sizeof(RGBTRIPLE)) % 4) % 4;

    //determine size for both header, bi & bf
    biOutfile.biSizeImage = ((sizeof(RGBTRIPLE) * biOutfile.biWidth + newPadding) * abs(biOutfile.biHeight));
    bfOutfile.bfSize = biOutfile.biSizeImage + sizeof(BITMAPINFOHEADER) + sizeof(BITMAPFILEHEADER);

    // printf("w: %i, h: %i, p: %i, sImage: %i, fSize: %i\n",
    //         bi.biWidth, bi.biHeight, oldPadding, bi.biSizeImage, bf.bfSize);
    // printf("w: %i, h: %i, p: %i, sImage: %i, fSize: %i\n",
    //         biOutfile.biWidth, biOutfile.biHeight, newPadding, biOutfile.biSizeImage, bfOutfile.bfSize);

    // write outfile's BITMAPFILEHEADER
    fwrite(&bfOutfile, sizeof(BITMAPFILEHEADER), 1, outptr);

    // write outfile's BITMAPINFOHEADER
    fwrite(&biOutfile, sizeof(BITMAPINFOHEADER), 1, outptr);

    // iterate over infile's scanlines
    for (int i = 0, biHeight = abs(bi.biHeight); i < biHeight; i++)
    {
        //Write one row n number of times to increase height
        for (int v = 0; v < n; v++)
        {
            // iterate over pixels in scanline
            for (int j = 0; j < bi.biWidth; j++)
            {
                // temporary storage
                RGBTRIPLE triple;

                // read RGB triple from infile
                fread(&triple, sizeof(RGBTRIPLE), 1, inptr);

                // Write pixel n number of times
                for (int d = 0; d < n; d++)
                {
                    //printf("v: %i, d: %i, B: %i, G: %i, R: %i ---- ", v, d, triple.rgbtBlue, triple.rgbtGreen, triple.rgbtRed);
                    // write RGB triple to outfile
                    fwrite(&triple, sizeof(triple), 1, outptr);
                }
            }

            // skip over padding, if any
            fseek(inptr, oldPadding, SEEK_CUR);

            // then add it back (to demonstrate how)
            for (int k = 0; k < newPadding; k++)
            {
                fputc(0x00, outptr);
            }

            if (v != (n-1))
            {
                //Calculate cursor pos to row start. -1 for seek backwards
                int rowLength = (sizeof(RGBTRIPLE) * bi.biWidth + oldPadding) * -1;
                fseek(inptr, rowLength, SEEK_CUR);
            }
        }
    }

    // close infile
    fclose(inptr);

    // close outfile
    fclose(outptr);

    // success
    return 0;
}
