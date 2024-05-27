/*
export default function AiView() {
  return (
    <div className="flex flex-col h-full items-center justify-center p-l text-center box-border">
      <img style={{ width: '200px' }} src="images/empty-plant.png" />
      <h2>This place intentionally left empty</h2>
      <p>It’s a place where you can grow your own UI 🤗</p>
    </div>
  );
}
*/

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
          onValueChanged={(e) => {
            setName(e.detail.value);
          }}
        />
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