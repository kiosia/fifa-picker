#!/usr/bin/env node

const fs = require('fs');
const { exec } = require('child_process');

fs.readdir('.', (error, files) => {
  files
    .filter((file) => /.*\.json/.test(file))
    .forEach((file) => {
      fs.readFile(file, (error, jsonString) => {
        const json = JSON.parse(jsonString);
        json.teams.forEach((team) => {
          const teamType = file.match(/([^_]+)_[0-9]+_stats\.json$/)[1];
          const teamName = team.name.replace(/[ ']/g, '_').toLowerCase();
          const teamCrestFileName = `${teamType}/${teamType}_${teamName}.png`;
          const teamCrestUrl = `${json.crest_base_url.replace('256', '50')}${team.crest_id}`;
          exec(`mkdir -p ${teamType}`);
          exec(`wget -O ${teamCrestFileName} ${teamCrestUrl}`, (error) => {
            if (error) {
              console.error(`Couldn't download ${team.name}'s crest: ${error}'`);
            }
          });
        });
      });
    });
});
