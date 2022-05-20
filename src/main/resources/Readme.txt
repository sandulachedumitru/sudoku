SUDOKU v1.1 - scrie rezultatele in fisierul SudokuResults.txt
===================================================
1.Programul gaseste toate solutiile unui joc de sudoku.
2.Fisierul Sudoku.xlsx este folosit ptr a initializa rapid un fisier de intrare ptr sudoku.
  Se face copy in xlsx apoi paste in fisierul destinatie. Primul element (nrElem) din fisier trebuie sa fie numarul de patrate (square). Un square contine nrElem celule.
  si tabela (board) cuprinde nrElem^2 square si nrElem^4 celule.
3.Fisierul trebuie sa fie de forma:

---------------------------------------------------
numarul de linii si coloane din zone sau square
3
linia   coloana valoare
1       4       6
1       7       1
1       8       4
1       9       2
2       2       2
2       3       6
3       1       7
3       6       4
3       9       9
4       6       6
4       7       2
5       2       6
5       4       9
5       6       1
5       8       5
6       3       5
6       4       3
7       1       9
7       4       4
7       9       7
8       7       8
8       8       1
9       1       8
9       2       4
9       3       2
9       6       7
---------------------------------------------------

  sau forma:

---------------------------------------------------
numarul de linii si coloane din zone sau square
3
coordonatele valorilor initiale: linia   coloana valoare
			1-4-6			1-7-1	1-8-4	1-9-2
	2-2-2	2-3-6						
3-1-7					3-6-4			3-9-9
					4-6-6	4-7-2		
	5-2-6		5-4-9		5-6-1		5-8-5	
		6-3-5	6-4-3					
7-1-9			7-4-4					7-9-7
						8-7-8	8-8-1	
9-1-8	9-2-4	9-3-2			9-6-7		
---------------------------------------------------

  sau alte forme. Textul si orice alte caractere in afara de numere nu va fi luat in seama. Daca coordonatele sunt incomplete (9-6- sau 9- in loc de 9-6-7) programul
  va da eroare. De asemenea daca nrElem==0 programul va da eraoare. Daca fisierul de initializare nu exista iarasi va da eroare. Etc...
4.Exista o varianta de program care scrie implicit rezultatele in fisierul SudokuResults.txt.
5.Pentru varianta de program care nu scrie implicit rezultatele si in fisierul SudokuResults.txt se poate folosi in linia de comanda (Windows) astfel:
  cd <directorul ce contine pachetele Controller, View, Model>
  java Controller.Main sudoku.txt > "myFile.ext"
  Toate rezultatele afisate pe ecran vor fi inregistrate si in myFile.ext (bineinteles ca fisierul poate avea orice nume si extensie se doreste).