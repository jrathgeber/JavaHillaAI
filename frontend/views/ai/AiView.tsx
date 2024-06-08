import { Button } from '@hilla/react-components/Button.js';
import { Notification } from '@hilla/react-components/Notification.js';
import { TextField } from '@hilla/react-components/TextField.js';
import { AiService } from 'Frontend/generated/endpoints.js';
import { useState } from 'react';

export default function AiView() {
  const [name, setName] = useState('');

  return (
    <>
      <section className="flex p-m gap-m items-end">
        <TextField
          label="Your Question"
          style={{ width: '100%' }}
          onValueChanged={(e) => {
            setName(e.detail.value);
          }}
        />
        <br/>
        <Button
          onClick={async () => {
            const serverResponse = await AiService.askQuestion(name);
            Notification.show(serverResponse);
          }}
        >
          Ask your question
        </Button>
      </section>
    </>
  );
}