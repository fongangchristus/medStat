export interface ISyntheseEvaluation {
    id?: number;
    composante?: string;
    pointDisponnible?: number;
    nbrIndicateurComposite?: number;
    pourcentageObtenue?: number;
    description?: string;
}

export class SyntheseEvaluation implements ISyntheseEvaluation {
    constructor(
        public id?: number,
        public composante?: string,
        public pointDisponnible?: number,
        public nbrIndicateurComposite?: number,
        public pourcentageObtenue?: number,
        public description?: string
    ) {}
}
