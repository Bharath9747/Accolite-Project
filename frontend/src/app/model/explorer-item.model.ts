export interface ExplorerItem {
  name: string;
  type: string;
  absolutePath: string;
  children: ExplorerItem[];
  expanded: boolean;
}
