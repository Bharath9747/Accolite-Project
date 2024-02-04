import { ExplorerItem } from './explorer-item.model';

export interface Question {
  title: string;
  paths: ExplorerItem[];
  type: string;
}
