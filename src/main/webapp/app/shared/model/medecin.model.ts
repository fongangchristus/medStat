export interface IMedecin {
    id?: number;
    nom?: string;
    telephone?: string;
    email?: string;
    qualification?: string;
    adresse?: string;
}

export class Medecin implements IMedecin {
    constructor(
        public id?: number,
        public nom?: string,
        public telephone?: string,
        public email?: string,
        public qualification?: string,
        public adresse?: string
    ) {}
}
