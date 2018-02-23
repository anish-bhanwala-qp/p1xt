// Implements a dictionary's functionality
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <ctype.h>

#include "dictionary.h"

void tolowercase(char *word)
{
    for(int i = 0; i < strlen(word); i++)
    {
        word[i] = tolower(word[i]);
    }
}


/* START Linked list nodes and methods*/
typedef struct linkedlistnode
{
    char word[46];
    struct linkedlistnode *next;
} node;


node * createnode(char *word)
{
    node *n = malloc(sizeof(node));
    n->next = NULL;
    strcpy(n->word, word);

    return n;
}

void addnode(node *n, char *word)
{
    node *newnode = createnode(word);
    if (n->next == NULL)
    {
        n->next = newnode;
    }
    else
    {
        //Make new node the first node;
        newnode->next = n->next;
        n->next = newnode;
    }
}

bool findword(node *n, const char *word)
{
      //Make sure word is lower case
    char copiedword[46];
    strcpy(copiedword, word);
    tolowercase(copiedword);

    while (n != NULL)
    {
        if (strcmp(copiedword, n->word) == 0)
        {
            return true;
        }
        n = n->next;
    }

    return false;
}

void freememory(node *n)
{
    while (n != NULL)
    {
        node *prev = n;
        n = n->next;
        free(prev);
    }
}

/* END Linked list nodes and methods*/

//node nodes[26];
node *firstnode = NULL;

bool check(const char *word)
{
    return findword(firstnode, word);
}

// Loads dictionary into memory, returning true if successful else false
bool load(const char *dictionary)
{

    FILE *fileptr = fopen(dictionary, "r");
    if (!fileptr)
    {
        return false;
    }

    char c;
    char word[LENGTH + 1];
    int i = 0;

    while ((c = getc(fileptr)) != EOF) {

        //Reset index if its's line end
        if (c == '\n')
        {
            //Terminate string
            word[i++] = '\0';
            i = 0;
            //add word to linked list
            if (firstnode == NULL)
            {
                firstnode = createnode(word);
            }
            else
            {
                addnode(firstnode, word);
            }
        }
        else
        {
            word[i++] = c;
        }

    }

    return true;
}


// Returns number of words in dictionary if loaded else 0 if not yet loaded
unsigned int size(void)
{
    int size = 0;
    node *next = firstnode;
    while (next != NULL)
    {
        size++;
        next = next->next;
    }
    return size;
}

// Unloads dictionary from memory, returning true if successful else false
bool unload(void)
{
    freememory(firstnode);
    return true;
}
