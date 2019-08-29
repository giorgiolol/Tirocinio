export interface ILibro {
  id?: number;
  titolo?: string;
  autore?: string;
  anno?: number;
}

export class Libro implements ILibro {
  constructor(public id?: number, public titolo?: string, public autore?: string, public anno?: number) {}
}
