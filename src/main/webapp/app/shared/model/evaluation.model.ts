import { Moment } from 'moment';

export interface IEvaluation {
    id?: number;
    lieu?: string;
    note?: number;
    dateEvaluation?: Moment;
    syntheseEvaluationId?: number;
    medecinEvaluationId?: number;
    questionnaireEvaluationId?: number;
}

export class Evaluation implements IEvaluation {
    constructor(
        public id?: number,
        public lieu?: string,
        public note?: number,
        public dateEvaluation?: Moment,
        public syntheseEvaluationId?: number,
        public medecinEvaluationId?: number,
        public questionnaireEvaluationId?: number
    ) {}
}
