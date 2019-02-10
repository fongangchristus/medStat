export interface ILocalisation {
    id?: number;
    region?: string;
    departement?: string;
    ville?: string;
    district?: string;
}

export class Localisation implements ILocalisation {
    constructor(public id?: number, public region?: string, public departement?: string, public ville?: string, public district?: string) {}
}
