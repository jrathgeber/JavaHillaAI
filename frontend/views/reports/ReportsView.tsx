import { Button } from '@hilla/react-components/Button.js';
import { Notification } from '@hilla/react-components/Notification.js';
import { TextField } from '@hilla/react-components/TextField.js';
import { TextArea } from '@hilla/react-components/TextArea.js';
import { ReportService } from 'Frontend/generated/endpoints.js';
import {useEffect, useState} from 'react';
import {Grid} from "@hilla/react-components/Grid";
import {GridColumn} from "@hilla/react-components/GridColumn";
import CloRecord from 'Frontend/generated/com/example/application/services/ReportService/CloReportRecord';
import { Link } from 'react-router-dom'


export default function ReportsView() {

  const [name, setName] = useState('');
  const [cloReports, setCloReports] = useState<CloReportRecord[]>([]);
  const [selected, setSelected] = useState<CloReportRecord | null | undefined>();

  useEffect(() => {
    ReportService.findAllCloReports().then(setCloReports);
  }, []);


  return (
    <>
      <section className="flex p-m gap-m items-end">
        <TextArea label="Ask a long question"

            style={{ width: '100%' }}

            onValueChanged={(e) => {
                setName(e.detail.value);
            }}

        />

        <Button
          onClick={async () => {
            const serverResponse = await ReportService.askQuestion(name);
            Notification.show(serverResponse, {
                duration: 0,
                theme: 'contrast',
                }
            );
            ReportService.findAllCloReports().then(setCloReports);
          }}
        >
          Ask your question
        </Button>
      </section>

      <div className="p-m flex gap-m">
        <Grid
            items={cloReports}
            onActiveItemChanged={e => setSelected(e.detail.value)}
            selectedItems={[selected]}>
            <GridColumn path="clo"/>
            <GridColumn path="name" autoWidth/>
            <GridColumn header="data" autoWidth>
            </GridColumn>
        </Grid>
      </div>
    </>
  );
}