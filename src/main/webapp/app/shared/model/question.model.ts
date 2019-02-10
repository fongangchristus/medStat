export interface IQuestion {
    id?: number;
    libele?: string;
    type?: string;
    questionnaireQuestionId?: number;
    categorieQuestionId?: number;
}

export class Question implements IQuestion {
    constructor(
        public id?: number,
        public libele?: string,
        public type?: string,
        public questionnaireQuestionId?: number,
        public categorieQuestionId?: number
    ) {}
}
