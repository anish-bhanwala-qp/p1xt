#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#include "linkedlist.h"

void addnode(linkedlist *ll, char *word)
{
    //Create newnode and allocate memory
    node *newnode = malloc(sizeof(node)); ;
    newnode->next = NULL;
    newnode->word = malloc(sizeof(char) * 46);
    strcpy(newnode->word, word);

    //printf("word: %s, copy: %s\n", word, newnode->word);

    node *first = ll->first;
    if (first == NULL)
    {
        //set newnode as first node
        ll->first = newnode;
    }
    else
    {
        //set newnode as first node and first node as 2nd node
        newnode->next = ll->first;
        ll->first = newnode;
    }
}

node *getlastnode(linkedlist *ll)
{
    //get first node
    node *last = ll->first;

    //loop until last is NULL or last node doesn't point to any node
    while (last != NULL && last->next != NULL)
    {
        last = last->next;
    }

    return last;
}

bool freememory(linkedlist *ll)
{
    node *n = ll->first;
    node *prev;
    while (n != NULL)
    {
        //printf("free memory: %s\n", n->word);
        n = (*n).next;
        prev = n;
        if (prev != NULL)
        {
            free(prev->word);
            free(prev);
        }
    }

    free(ll);

    return true;
}

bool findword(linkedlist *ll, const char *word)
{
    //Make sure word is lower case
    char copiedword[45 + 1];
    strcpy(copiedword, word);
    tolowercase(copiedword);

    node *n = ll->first;
    while (n != NULL)
    {
        if (strcmp(copiedword, n->word) == 0)
        {
            //printf("word found %s\n", word);
            return true;
        }
        n = n->next;
    }

    //printf("word not found %s\n", word);
    return false;
}

void printall(linkedlist *ll)
{
    int i = 1;
    node *n = (*ll).first;
    while (n != NULL && (*n).next != NULL)
    {
        //printf("node %i, %p, %s\n", i, n, (*n).word);
        n = (*n).next;
        i++;

        if (i == 10)
        {
            break;
        }
    }
    //printf("node %i, %p, %s\n", i, n, (*n).word);
}

void tolowercase(char *word)
{
    for(int i = 0; i < strlen(word); i++)
    {
        word[i] = tolower(word[i]);
    }
}

int sizeoflinkedlist(linkedlist *ll)
{
    int i = 0;
    node *n = (*ll).first;
    while (n != NULL)
    {
        i++;
        n = (*n).next;
    }

    return i;
}
