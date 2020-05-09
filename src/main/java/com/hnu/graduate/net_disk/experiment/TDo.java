package com.hnu.graduate.net_disk.experiment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author muyunhao
 * @date 2020/5/2 12:10 下午
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TDo implements Serializable {
    int e;
    int y;
    int v;
}
