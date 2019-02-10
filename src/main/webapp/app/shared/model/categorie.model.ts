export interface ICategorie {
    id?: number;
    libele?: string;
    description?: string;
}

export class Categorie implements ICategorie {
    constructor(public id?: number, public libele?: string, public description?: string) {}
}
