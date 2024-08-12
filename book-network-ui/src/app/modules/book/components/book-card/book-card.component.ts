import { Component, Input } from '@angular/core';
import { BookResponse } from 'src/app/services/models';

@Component({
  selector: 'app-book-card',
  templateUrl: './book-card.component.html',
  styleUrls: ['./book-card.component.scss']
})
export class BookCardComponent {

  private _book: BookResponse = {}; 
  private _bookCover: string | undefined;

  get book(): BookResponse {
    return this._book;
  }

  get bookCover(): string | undefined {
    if (this._book.cover){
      return 'data:image/jpg;base64,'+this._book.cover;
    }
    //return this._bookCover;
    return 'https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg';
  }

  @Input()
  set book(value: BookResponse) {
    this._book = value;
  }



}
