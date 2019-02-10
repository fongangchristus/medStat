export const enum TypeEtab {
    PUBLIQUE = 'PUBLIQUE',
    PRIVEE = 'PRIVEE'
}

export interface IEtablissement {
    id?: number;
    libele?: string;
    description?: string;
    type?: TypeEtab;
    nbrMedecin?: number;
    nbrPersonnelSfIsIdeTms?: number;
    nbrPersonnelIbIbaTal?: number;
    nbrPersonnelAsAtms?: number;
    nbrPersonnelAdministratif?: number;
    nbrPersonnelNomQualifie?: number;
    ratioPersonnelQualifieparHab?: number;
    nbrLitparHabitant?: number;
    penta1?: number;
    penta3?: number;
    difPenta1Et3?: number;
    medecinId?: number;
    localisationEtablissementId?: number;
}

export class Etablissement implements IEtablissement {
    constructor(
        public id?: number,
        public libele?: string,
        public description?: string,
        public type?: TypeEtab,
        public nbrMedecin?: number,
        public nbrPersonnelSfIsIdeTms?: number,
        public nbrPersonnelIbIbaTal?: number,
        public nbrPersonnelAsAtms?: number,
        public nbrPersonnelAdministratif?: number,
        public nbrPersonnelNomQualifie?: number,
        public ratioPersonnelQualifieparHab?: number,
        public nbrLitparHabitant?: number,
        public penta1?: number,
        public penta3?: number,
        public difPenta1Et3?: number,
        public medecinId?: number,
        public localisationEtablissementId?: number
    ) {}
}
