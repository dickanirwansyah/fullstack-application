import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PageResponseBookResponse } from 'src/app/services/models';
import { BookService } from 'src/app/services/services';

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.scss']
})
export class BookListComponent implements OnInit{

  bookResponse: PageResponseBookResponse = {};
  private page = 0;
  private size = 5;

  constructor(private bookService:BookService,
    private router:Router
  ){}

  ngOnInit(): void {
    this.findAllBooks();
  }

  private findAllBooks(){
    this.bookService.findAllBooks({page: this.page,size: this.size})
      .subscribe({
        next: (books:PageResponseBookResponse) => {
          this.bookResponse = books;
        }
      })
  }

}
